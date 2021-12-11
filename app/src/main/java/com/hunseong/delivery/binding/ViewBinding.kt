package com.hunseong.delivery.binding

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hunseong.delivery.R
import com.hunseong.delivery.data.model.Company
import com.hunseong.delivery.data.model.Result
import com.hunseong.delivery.extension.gone
import com.hunseong.delivery.extension.visible


object ViewBinding {

    @JvmStatic
    @BindingAdapter("isLoading")
    fun bindIsLoading(view: View, result: Result<Any>) {
        view.isVisible = result is Result.Loading
    }

    @JvmStatic
    @BindingAdapter("toast")
    fun bindToast(view: View, result: Result<Any>) {
        if (result is Result.Error) {
            when {
                result.isNetworkError -> {
                    Toast.makeText(view.context, R.string.network_error, Toast.LENGTH_SHORT).show()
                }
                result.msg != null -> {
                    Toast.makeText(view.context, result.msg, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(view.context, R.string.undefined_error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    @JvmStatic
    @BindingAdapter("backPressed")
    fun bindBackPressed(view: View, isBackPressed: Boolean) {
        if (isBackPressed) {
            view.setOnClickListener {


                // Hide Keyboard
                val inputMethodManager =
                    view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                view.findNavController().navigateUp()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("companyList")
    fun bindCompanyList(view: Spinner, result: Result<List<Company>>) {

        if (result is Result.Success) {

            // Spinner items<String>
            val items = result.data.map { it.name }

            // Spinner Adapter
            val adapter = object : ArrayAdapter<String>(view.context,
                android.R.layout.simple_spinner_dropdown_item) {

                override fun getCount(): Int = super.getCount() - 1
            }
            adapter.addAll(items)
            adapter.add("택배사 선택")


            // Spinner adapter , listener, initial selection setting
            view.adapter = adapter

            view.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    selected: View?,
                    position: Int,
                    id: Long,
                ) {
                    (view.rootView.findViewById<EditText>(R.id.invoice_et).text.toString().length >= 9).also {
                        view.rootView.findViewById<Button>(R.id.add_btn).isEnabled = it
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    view.rootView.findViewById<Button>(R.id.add_btn).isEnabled = false
                }

            }
            view.setSelection(adapter.count)
        }
    }

    @JvmStatic
    @BindingAdapter("recommendList")
    fun bindRecommendList(view: ChipGroup, result: Result<List<Company>>) {
        if (result is Result.Error || result is Result.Empty) {
            view.gone()
            view.rootView.findViewById<TextView>(R.id.recommend_tv).gone()
        } else if (result is Result.Success) {
            view.visible()
            view.rootView.findViewById<TextView>(R.id.recommend_tv).visible()
            view.removeAllViews()
            result.data.map { company ->
                view.addView(Chip(view.context).apply {
                    text = company.name
                    setOnClickListener {
                        val spinner = view.rootView.findViewById<Spinner>(R.id.company_spinner)

                        val position =
                            (spinner.adapter as ArrayAdapter<String>).getPosition(company.name)

                        spinner.setSelection(position)
                    }
                })
            }
        }
    }
}