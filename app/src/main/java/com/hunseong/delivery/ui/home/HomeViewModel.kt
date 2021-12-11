package com.hunseong.delivery.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hunseong.delivery.data.model.Result
import com.hunseong.delivery.data.model.TrackingInfoCompany
import com.hunseong.delivery.data.repository.FirestoreRepository
import com.hunseong.delivery.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {

    private val _trackingInfos =
        MutableStateFlow<Result<List<TrackingInfoCompany>>>(Result.Uninitialized)
    val trackingInfos = _trackingInfos.asStateFlow()

    fun updateInfo(uid: String) = viewModelScope.launch {
        _trackingInfos.value = Result.Loading
        try {
            val searchInfos = firestoreRepository.getAllSearchInfo(uid)
            val trackingInfoCompany = homeRepository.getAllTrackingInfo(searchInfos)
            if (trackingInfoCompany.isEmpty()) {
                _trackingInfos.value = Result.Empty
            } else {
                val sortedInfoCompany = trackingInfoCompany.sortedWith(
                    compareBy(
                        { it.info!!.completeYN },
                        { -(it.info!!.lastDetail!!.time ?: Long.MAX_VALUE) }
                    )
                )
                _trackingInfos.value =
                    Result.Success(sortedInfoCompany)
            }
        } catch (e: Exception) {
            _trackingInfos.value = Result.Error(e)
        }
    }

    fun deleteInfo(uid: String, infoList: List<TrackingInfoCompany>) = viewModelScope.launch {
        _trackingInfos.value = Result.Loading
        try {
            infoList.forEach {
                firestoreRepository.deleteInfo(uid, it.info!!.invoiceNo!!)
            }
            updateInfo(uid)
        } catch (e: Exception) {
            _trackingInfos.value = Result.Error(e)
        }
    }
}