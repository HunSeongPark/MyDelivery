package com.hunseong.delivery.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrackingSearchInfo(
    val invoice: String? = null,
    val company: Company? = null
) : Parcelable