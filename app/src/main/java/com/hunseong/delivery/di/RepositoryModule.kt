package com.hunseong.delivery.di

import com.hunseong.delivery.data.api.DeliveryApi
import com.hunseong.delivery.data.api.FirestoreApi
import com.hunseong.delivery.data.repository.AddRepository
import com.hunseong.delivery.data.repository.FirestoreRepository
import com.hunseong.delivery.data.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(ViewModelComponent::class)
@Module
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideAddRepository(
        deliveryApi: DeliveryApi,
        ioDispatcher: CoroutineDispatcher,
    ): AddRepository = AddRepository(deliveryApi, ioDispatcher)

    @Provides
    @ViewModelScoped
    fun provideFirestoreRepository(
        firestoreApi: FirestoreApi,
        ioDispatcher: CoroutineDispatcher,
    ): FirestoreRepository = FirestoreRepository(firestoreApi, ioDispatcher)

    @Provides
    @ViewModelScoped
    fun provideHomeRepository(
        deliveryApi: DeliveryApi,
        ioDispatcher: CoroutineDispatcher,
    ): HomeRepository = HomeRepository(deliveryApi, ioDispatcher)
}