package com.tech.indiaekartshoppinguser.data.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.razorpay.Checkout
import com.tech.indiaekartshoppinguser.MainActivity
import com.tech.indiaekartshoppinguser.data.PushNotification
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideFirebaseStore() : FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun provideFirebaseStorage() : FirebaseStorage{
        return FirebaseStorage.getInstance()
    }
    @Provides
    @Singleton
    fun provideFirebaseMessaging() : FirebaseMessaging{
        return FirebaseMessaging.getInstance()
    }

    @Provides
    fun provideSignInClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    @Singleton
    fun providePushNotification(
        firebaseFireStore: FirebaseFirestore,
        @ApplicationContext context: Context,
        firebaseAuth: FirebaseAuth
    ): PushNotification {
        return PushNotification(firebaseFireStore,firebaseAuth,context)
    }

    @Provides
    @Singleton
    fun provideCheckout(@ApplicationContext context: Context): Checkout {
        // Initialize and preload Razorpay Checkout
        Checkout.preload(context)
        return Checkout()
    }
}