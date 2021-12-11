package com.hunseong.delivery.data.repository

import com.hunseong.delivery.data.api.FirestoreApi
import com.hunseong.delivery.data.model.TrackingSearchInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val firestoreApi: FirestoreApi,
    private val ioDispatcher: CoroutineDispatcher,
) : Repository {

    suspend fun getAllSearchInfo(uid: String): List<TrackingSearchInfo> =
        withContext(ioDispatcher) {
            firestoreApi.getAllSearchInfo(uid)
        }

    fun insertSearchInfo(uid: String, searchInfo: TrackingSearchInfo) {
        firestoreApi.insertSearchInfo(uid, searchInfo)
    }
}