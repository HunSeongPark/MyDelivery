package com.hunseong.delivery.binding

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
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
    @BindingAdapter("backPressed")
    fun bindBackPressed(view: View, isBackPressed: Boolean) {
        if (isBackPressed) {
            view.setOnClickListener {
                view.findNavController().navigateUp()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("companyList")
    fun bindCompanyList(view: Spinner, result: Result<List<Company>>) {

        if (result is Result.Success) {
            val items = result.data.map { it.name }

            val adapter = object : ArrayAdapter<String>(view.context,
                android.R.layout.simple_spinner_dropdown_item) {

                override fun getCount(): Int = super.getCount() - 1
            }

            adapter.addAll(items)
            adapter.add("택배사 선택")

            view.adapter = adapter
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