package com.tech.indiaekartshoppinguser.presentation.di

import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.tech.indiaekartshoppinguser.common.OneTapSignInResult
import com.tech.indiaekartshoppinguser.data.PushNotification
import com.tech.indiaekartshoppinguser.data.repositoryImpl.ShoppingRepositoryImpl
import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UiModule {

    @Provides
    @Singleton
    fun provideRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        firebaseStorage: FirebaseStorage,
        firebaseMessaging: FirebaseMessaging,
        oneTapSignInClient: SignInClient,
        pushNotification: PushNotification
    ): ShoppingRepository {
        return ShoppingRepositoryImpl(firestore, firebaseAuth, firebaseStorage,firebaseMessaging,oneTapSignInClient,pushNotification)
    }
}