package com.hunseong.delivery.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hunseong.delivery.data.model.TrackingDetail
import com.hunseong.delivery.databinding.ItemFirstBinding
import com.hunseong.delivery.databinding.ItemLastBinding
import com.hunseong.delivery.databinding.ItemMiddleBinding

class DetailAdapter : ListAdapter<TrackingDetail, RecyclerView.ViewHolder>(diffUtil) {

    inner class FirstViewHolder(private val binding: ItemFirstBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(info: TrackingDetail) {
            binding.info = info
        }
    }

    inner class MiddleViewHolder(private val binding: ItemMiddleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(info: TrackingDetail) {
            binding.info = info
        }
    }

    inner class LastViewHolder(private val binding: ItemLastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(info: TrackingDetail) {
            binding.info = info
        }
    }

    companion object {
        const val ITEM_FIRST = 0
        const val ITEM_MIDDLE = 1
        const val ITEM_LAST = 2

        val diffUtil = object : DiffUtil.ItemCallback<TrackingDetail>() {
            override fun areItemsTheSame(oldItem: TrackingDetail, newItem: TrackingDetail): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(oldItem: TrackingDetail, newItem: TrackingDetail): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_FIRST -> FirstViewHolder(ItemFirstBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
            ITEM_MIDDLE -> MiddleViewHolder(ItemMiddleBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
            else -> LastViewHolder(ItemLastBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FirstViewHolder -> holder.bind(getItem(position))
            is MiddleViewHolder -> holder.bind(getItem(position))
            is LastViewHolder -> holder.bind(getItem(position))
            else -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_FIRST
            currentList.size - 1 -> ITEM_LAST
            else -> ITEM_MIDDLE
        }
    }
}
