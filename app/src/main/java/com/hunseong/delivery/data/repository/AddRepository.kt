package com.hunseong.delivery.data.repository

import com.hunseong.delivery.data.model.Result
import com.hunseong.delivery.data.network.DeliveryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddRepository @Inject constructor(
    private val deliveryApi: DeliveryApi,
) : Repository {

    fun getCompanies() = flow {
        emit(Result.Loading)
        val response = deliveryApi.getCompanies()
        if (response.companies == null) {
            emit(Result.Error(RuntimeException()))
        } else {
            emit(Result.Success(response.companies))
        }
    }.catch { e -> emit(Result.Error(e)) }

    fun getRecommendCompanies(invoice: String) = flow {
        emit(Result.Loading)
        val response = deliveryApi.getRecommendCompanies(invoice = invoice)
        if (response.errorMessage != null && response.companies == null) {
            emit(Result.Error(RuntimeException(), response.errorMessage))
        } else {
            if (response.companies!!.isEmpty()) {
                emit(Result.Empty)
            } else {
                emit(Result.Success(response.companies))
            }
        }
    }.catch { e -> emit(Result.Error(e)) }

}