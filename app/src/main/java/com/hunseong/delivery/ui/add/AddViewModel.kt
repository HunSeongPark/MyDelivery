package com.hunseong.delivery.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hunseong.delivery.data.model.Company
import com.hunseong.delivery.data.model.Result
import com.hunseong.delivery.data.model.TrackingSearchInfo
import com.hunseong.delivery.data.repository.AddRepository
import com.hunseong.delivery.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addRepository: AddRepository,
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {

    private val query = MutableStateFlow("")

    val companies: StateFlow<Result<List<Company>>> =
        addRepository.getCompanies()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Result.Uninitialized
            )

    val recommends: StateFlow<Result<List<Company>>> = query
        .debounce(700)
        .filter { it.isNotBlank() }
        .flatMapLatest { query ->
            addRepository.getRecommendCompanies(query)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Uninitialized
        )

    private val _trackingInfo = MutableStateFlow<Result<TrackingSearchInfo>>(Result.Uninitialized)
    val trackingInfo = _trackingInfo.asStateFlow()


    fun addInvoice(uid: String, invoice: String, companyName: String) = viewModelScope.launch {
        _trackingInfo.value = Result.Loading
        try {
            val company = (companies.value as Result.Success<List<Company>>).data
                .filter { it.name == companyName }.getOrNull(0)

            if (company == null) {
                _trackingInfo.value = Result.Error(RuntimeException(), "올바르지 않은 택배사 코드 정보입니다.")
                return@launch
            }

            val info = addRepository.getTrackingInfo(invoice, company.code!!)
            when {
                info.errorMessage != null -> {
                    _trackingInfo.value = Result.Error(RuntimeException(), info.errorMessage)
                }
                info.lastDetail == null -> {
                    _trackingInfo.value = Result.Error(RuntimeException(), "등록되지 않은 운송장 번호입니다.")
                }
                else -> {
                    val searchInfo = TrackingSearchInfo(invoice, company)
                    insertSearchInfo(uid, searchInfo)
                    _trackingInfo.value = Result.Success(searchInfo)
                }
            }

        } catch (e: Exception) {
            _trackingInfo.value = Result.Error(e)
        }
    }

    private fun insertSearchInfo(uid: String, searchInfo: TrackingSearchInfo) {
        firestoreRepository.insertSearchInfo(uid, searchInfo)
    }

    fun changeQuery(query: String) {
        this.query.value = query
    }
}