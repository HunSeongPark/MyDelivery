package com.hunseong.delivery.ui.add

import android.app.Activity
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hunseong.delivery.R
import com.hunseong.delivery.data.model.Result
import com.hunseong.delivery.databinding.FragmentAddBinding
import com.hunseong.delivery.extension.gone
import com.hunseong.delivery.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding

    private lateinit var auth : FirebaseAuth

    private val viewModel: AddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        auth = Firebase.auth

        binding = FragmentAddBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }

        initViews()
        observeData()
        return binding.root
    }

    private fun initViews() {

        // Invoice EditText listener
        binding.invoiceEt.addTextChangedListener {
            if (it.toString().length >= 12) {
                viewModel.changeQuery(it.toString())
                if (binding.companySpinner.selectedItemPosition != binding.companySpinner.count) {
                    binding.addBtn.isEnabled = true
                }
            } else {
                binding.addBtn.isEnabled = false
            }
        }

        // Add Button
        binding.addBtn.setOnClickListener {
            val invoice = binding.invoiceEt.text.toString()
            val companyName = binding.companySpinner.selectedItem as String
            viewModel.addInvoice(auth.currentUser!!.uid, invoice, companyName)
        }

        copyClipBoardInvoice()
    }

    private fun copyClipBoardInvoice() {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val invoice = clipboard.plainTextClip()
        if (!invoice.isNullOrBlank() && invoice.isDigitsOnly()) {
            AlertDialog.Builder(requireActivity())
                .setTitle("클립 보드에 있는 $invoice 를 운송장 번호로 추가하기")
                .setPositiveButton("추가") { _, _ ->
                    binding.invoiceEt.setText(invoice)
                }
                .setNegativeButton("취소") { _, _ -> }
                .create()
                .show()
        }
    }

    private fun ClipboardManager.plainTextClip(): String? =
        if (hasPrimaryClip() && (primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true)) {
            primaryClip?.getItemAt(0)?.text?.toString()
        } else {
            null
        }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.trackingInfo.collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visible()
                        }
                        is Result.Success -> {
                            binding.progressBar.gone()
                            Timber.tag("infoResult").d(result.data.toString())
                            Toast.makeText(this@AddFragment.context,
                                R.string.add_invoice_complete,
                                Toast.LENGTH_SHORT)
                                .show()

                            // Hide Keyboard
                            val inputMethodManager =
                                requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
                            findNavController().navigateUp()
                        }
                        is Result.Error -> {
                            binding.progressBar.gone()
                            when {
                                result.msg != null -> {
                                    Toast.makeText(this@AddFragment.context,
                                        result.msg,
                                        Toast.LENGTH_SHORT)
                                        .show()
                                }
                                result.isNetworkError -> {
                                    Toast.makeText(this@AddFragment.context,
                                        R.string.network_error,
                                        Toast.LENGTH_SHORT)
                                        .show()
                                }
                                else -> {
                                    Toast.makeText(this@AddFragment.context,
                                        R.string.undefined_error,
                                        Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}