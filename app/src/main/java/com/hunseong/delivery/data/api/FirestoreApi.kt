package com.hunseong.delivery.data.api

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.hunseong.delivery.data.model.TrackingSearchInfo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreApi @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    suspend fun getAllSearchInfo(uid: String): List<TrackingSearchInfo> {

        return firestore.collection(uid)
            .get()
            .await()
            .map { it.toObject() }
    }


    fun insertInfo(uid: String, searchInfo: TrackingSearchInfo) {
        firestore.collection(uid)
            .document(searchInfo.invoice!!)
            .set(searchInfo)
    }

    fun deleteInfo(uid: String, invoice: String) {
        firestore.collection(uid)
            .document(invoice)
            .delete()
    }

}