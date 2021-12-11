package com.hunseong.delivery.data.repository

import com.hunseong.delivery.data.model.Result
import com.hunseong.delivery.data.model.TrackingInformation
import com.hunseong.delivery.data.network.DeliveryApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddRepository @Inject constructor(
    private val deliveryApi: DeliveryApi,
    private val ioDispatcher: CoroutineDispatcher
) : Repository {

    fun getCompanies() = flow {
        emit(Result.Loading)
        val response = deliveryApi.getCompanies()
        if (response.companies == null && response.errorMsg != null) {
            emit(Result.Error(RuntimeException(), response.errorMsg))
        } else {
            emit(Result.Success(response.companies!!))
        }
    }.catch { e -> emit(Result.Error(e)) }


    fun getRecommendCompanies(invoice: String) = flow {
        emit(Result.Loading)
        val response = deliveryApi.getRecommendCompanies(invoice = invoice)
        if (response.errorMsg != null && response.companies == null) {
            emit(Result.Error(RuntimeException(), response.errorMsg))
        } else {
            if (response.companies!!.isEmpty()) {
                emit(Result.Empty)
            } else {
                emit(Result.Success(response.companies))
            }
        }
    }.catch { e -> emit(Result.Error(e)) }


    suspend fun getTrackingInfo(invoice: String, companyCode: String) = withContext(ioDispatcher) {
        deliveryApi.getTrackingInfo(invoice = invoice, code = companyCode)
    }

}