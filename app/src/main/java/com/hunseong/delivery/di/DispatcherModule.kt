package com.hunseong.delivery.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DispatcherModule {
    @Provides
    @Singleton
    fun provideIoDispatcher() : CoroutineDispatcher = Dispatchers.IO
}