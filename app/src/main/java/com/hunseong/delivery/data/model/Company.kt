package com.hunseong.delivery.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Company(
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("Code")
    val code: String? = null,
) : Parcelable
