package com.hunseong.delivery.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
            if (it.toString().length >= 9) {
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