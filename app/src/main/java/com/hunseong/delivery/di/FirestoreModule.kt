package com.hunseong.delivery.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirestoreModule {
    @Provides
    @Singleton
    fun provideDB() : FirebaseFirestore = FirebaseFirestore.getInstance()
}