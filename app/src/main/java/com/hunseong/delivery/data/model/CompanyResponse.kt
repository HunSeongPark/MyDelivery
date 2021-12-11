package com.hunseong.delivery.data.model

import com.google.gson.annotations.SerializedName

data class CompanyResponse(
    @SerializedName("Company") val companies: List<Company>? = null,
)
