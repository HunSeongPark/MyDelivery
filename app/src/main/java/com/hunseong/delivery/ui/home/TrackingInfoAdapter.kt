package com.hunseong.delivery.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hunseong.delivery.R
import com.hunseong.delivery.data.model.TrackingInfoCompany
import com.hunseong.delivery.databinding.ItemDeliveryBinding

class TrackingInfoAdapter :
    ListAdapter<TrackingInfoCompany, TrackingInfoAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemDeliveryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition.takeIf { it != NO_POSITION }
                    ?: return@setOnClickListener
                val trackingInformation = getItem(position)
                // TODO 아이템 클릭 시 Detail 이동
            }
        }

        fun bind(info: TrackingInfoCompany) {
            binding.information = info

            if (info.info!!.completeYN == "Y") {
                binding.dateTv.setTextColor(ContextCompat.getColor(binding.root.context,
                    R.color.divider_gray))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemDeliveryBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TrackingInfoCompany>() {
            override fun areItemsTheSame(
                oldItem: TrackingInfoCompany,
                newItem: TrackingInfoCompany,
            ): Boolean = oldItem.info!!.invoiceNo == newItem.info!!.invoiceNo

            override fun areContentsTheSame(
                oldItem: TrackingInfoCompany,
                newItem: TrackingInfoCompany,
            ): Boolean = oldItem == newItem
        }
    }
}