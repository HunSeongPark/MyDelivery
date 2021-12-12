package com.hunseong.delivery.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hunseong.delivery.R
import com.hunseong.delivery.data.model.Level
import com.hunseong.delivery.databinding.FragmentDetailBinding
import com.hunseong.delivery.extension.gone
import com.hunseong.delivery.extension.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding

    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentDetailBinding.inflate(inflater, container, false).apply {
            info = args.info
        }

        initViews()

        return binding.root
    }

    private fun initViews() = with(binding) {

        val details = args.info.info?.trackingDetails

        if (details == null) {
            emptyInfoTv.visible()
            recyclerView.gone()
            return@with
        }

        recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@DetailFragment.context, RecyclerView.VERTICAL, true)
                    .apply {
                        stackFromEnd = true
                    }
            adapter = DetailAdapter().apply {
                submitList(details)
            }
        }


        when (details.last().level) {
            Level.PREPARE, Level.READY_FOR_SHIPPING -> {
                binding.itemGetIv.setBackgroundResource(R.drawable.history_complete_background)
            }
            Level.ON_TRANSIT -> {
                binding.itemMoveIv.setBackgroundResource(R.drawable.history_complete_background)
            }
            Level.ON_ARRIVE_ROUTER -> {
                binding.itemArriveCompanyIv.setBackgroundResource(R.drawable.history_complete_background)
            }
            Level.START -> {
                binding.itemStartDeliveryIv.setBackgroundResource(R.drawable.history_complete_background)
            }
            Level.COMPLETE -> {
                binding.itemArriveHomeIv.setBackgroundResource(R.drawable.history_complete_background)
            }
        }
    }
}
