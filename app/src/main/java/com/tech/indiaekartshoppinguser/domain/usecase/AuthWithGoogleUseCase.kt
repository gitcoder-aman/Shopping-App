package com.tech.indiaekartshoppinguser.domain.usecase

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import javax.inject.Inject

class AuthWithGoogleUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    fun oneTapSignIn(activity: Activity,signInLauncher: ActivityResultLauncher<IntentSenderRequest>) = shoppingRepository.initiateOneTapSignIn(activity,signInLauncher)
    fun googleFirebaseLogin(idToken : String) = shoppingRepository.firebaseWithGoogle(idToken)
}