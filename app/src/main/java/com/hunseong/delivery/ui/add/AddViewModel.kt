package com.hunseong.delivery.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hunseong.delivery.data.model.Company
import com.hunseong.delivery.data.model.Result
import com.hunseong.delivery.data.model.TrackingInformation
import com.hunseong.delivery.data.repository.AddRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addRepository: AddRepository,
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

    private val _trackingInfo = MutableStateFlow<Result<TrackingInformation>>(Result.Uninitialized)
    val trackingInfo = _trackingInfo.asStateFlow()


    fun addInvoice(invoice: String, companyName: String) = viewModelScope.launch {
        _trackingInfo.value = Result.Loading
        try {
            val companyCode = (companies.value as Result.Success<List<Company>>).data
                .filter { it.name == companyName }.getOrNull(0)?.code

            if (companyCode == null) {
                _trackingInfo.value = Result.Error(RuntimeException(), "올바르지 않은 택배사 코드 정보입니다.")
                return@launch
            }
            val info = addRepository.getTrackingInfo(invoice, companyCode)
            if (info.errorMessage != null) {
                _trackingInfo.value = Result.Error(RuntimeException(), info.errorMessage)
            } else {
                _trackingInfo.value = Result.Success(info)
            }
        } catch (e: Exception) {
            _trackingInfo.value = Result.Error(e)
        }

    }

    fun changeQuery(query: String) {
        this.query.value = query
    }
}