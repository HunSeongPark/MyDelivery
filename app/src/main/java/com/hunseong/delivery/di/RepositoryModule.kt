package com.hunseong.delivery.di

import com.hunseong.delivery.data.network.DeliveryApi
import com.hunseong.delivery.data.repository.AddRepository
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
        ioDispatcher: CoroutineDispatcher
    ): AddRepository = AddRepository(deliveryApi, ioDispatcher)
}