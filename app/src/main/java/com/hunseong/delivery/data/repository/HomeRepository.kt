package com.hunseong.delivery.data.repository

import com.hunseong.delivery.data.api.DeliveryApi
import com.hunseong.delivery.data.model.TrackingInfoCompany
import com.hunseong.delivery.data.model.TrackingSearchInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val deliveryApi: DeliveryApi,
    private val ioDispatcher: CoroutineDispatcher,
) : Repository {

    suspend fun getAllTrackingInfo(searchInfos: List<TrackingSearchInfo>) =
        withContext(ioDispatcher) {
            searchInfos.map {
                TrackingInfoCompany(
                    deliveryApi.getTrackingInfo(invoice = it.invoice!!, code = it.company!!.code!!),
                    it.company
                )
            }
        }

}