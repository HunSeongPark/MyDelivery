package com.hunseong.delivery.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hunseong.delivery.data.model.Company
import com.hunseong.delivery.data.model.Result
import com.hunseong.delivery.data.repository.AddRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
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

    fun changeQuery(query: String) {
        this.query.value = query
    }
}