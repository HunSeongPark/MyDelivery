package com.hunseong.delivery.data.model

import com.google.gson.annotations.SerializedName

data class RecommendResponse(
    @SerializedName("Recommend") val companies: List<Company>? = null,
    @SerializedName("msg") val errorMessage: String? = null,
)
