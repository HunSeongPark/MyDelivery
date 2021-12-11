package com.hunseong.delivery.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.hunseong.delivery.data.api.DeliveryApi
import com.hunseong.delivery.data.api.FirestoreApi
import com.hunseong.delivery.data.api.HttpRequestInterceptor
import com.hunseong.delivery.data.api.Url.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpRequestInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    @Provides
    @Singleton
    fun provideDeliveryApi(retrofit: Retrofit): DeliveryApi {
        return retrofit.create(DeliveryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFirestoreApi(firestore: FirebaseFirestore) : FirestoreApi {
        return FirestoreApi(firestore)
    }
}