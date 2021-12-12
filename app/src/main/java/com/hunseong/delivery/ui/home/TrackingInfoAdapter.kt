package com.hunseong.delivery.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hunseong.delivery.R
import com.hunseong.delivery.data.model.TrackingInfoCompany
import com.hunseong.delivery.databinding.ItemDeliveryBinding

class TrackingInfoAdapter(private val onClick: (TrackingInfoCompany) -> Unit) :
    ListAdapter<TrackingInfoCompany, TrackingInfoAdapter.ViewHolder>(diffUtil) {

    var modifyMode: Boolean = false

    inner class ViewHolder(private val binding: ItemDeliveryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition.takeIf { it != NO_POSITION }
                    ?: return@setOnClickListener
                onClick(getItem(position))
            }
        }

        fun bind(info: TrackingInfoCompany) {
            val position = bindingAdapterPosition.takeIf { it != NO_POSITION } ?: return

            binding.information = info
            binding.checkBox.isVisible = modifyMode

            binding.checkBox.setOnCheckedChangeListener(null)

            binding.checkBox.isChecked = getItem(position).isChecked

            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                getItem(position).isChecked = isChecked
            }

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