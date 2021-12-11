package com.hunseong.delivery.data.network

import com.hunseong.delivery.BuildConfig
import com.hunseong.delivery.data.model.CompanyResponse
import com.hunseong.delivery.data.model.RecommendResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DeliveryApi {

    @GET("/api/v1/companylist")
    suspend fun getCompanies(
        @Query("t_key") key: String = BuildConfig.SWEET_TRACKER_API_KEY,
    ): CompanyResponse

    @GET("/api/v1/recommend")
    suspend fun getRecommendCompanies(
        @Query("t_key") key: String = BuildConfig.SWEET_TRACKER_API_KEY,
        @Query("t_invoice") invoice: String
    ): RecommendResponse
}