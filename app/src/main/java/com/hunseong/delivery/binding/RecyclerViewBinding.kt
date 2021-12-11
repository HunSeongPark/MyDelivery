package com.hunseong.delivery.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hunseong.delivery.data.model.Result
import com.hunseong.delivery.data.model.TrackingInfoCompany
import com.hunseong.delivery.ui.home.TrackingInfoAdapter


object RecyclerViewBinding {

    @JvmStatic
    @BindingAdapter("submitList")
    fun bindSubmitList(view: RecyclerView, result: Result<Any>) {
        if (result is Result.Success) {
            when (view.adapter) {
                is TrackingInfoAdapter -> {
                    (view.adapter as TrackingInfoAdapter).apply {
                        submitList(result.data as List<TrackingInfoCompany>)
                        modifyMode = false
                    }
                }
            }
        } else if(result is Result.Empty || result is Result.Error) {
            when (view.adapter) {
                is TrackingInfoAdapter -> {
                    (view.adapter as TrackingInfoAdapter).apply {
                        submitList(emptyList())
                        modifyMode = false
                    }
                }
            }
        }
    }
}