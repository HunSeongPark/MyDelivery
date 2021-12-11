package com.hunseong.delivery.data.model

import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName("Name")
    val name: String,
    @SerializedName("Code")
    val code: String,
)
