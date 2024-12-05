package com.tech.indiaekartshoppinguser.common

sealed class ResultState<out T>{
    data class Success<T>(val data : T) : ResultState<T>()
    data class Error(val error : String) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
}

sealed class OneTapSignInResult {
    object Idle : OneTapSignInResult()
    object Loading : OneTapSignInResult()
    data class FirebaseSuccess(val isSignedIn: Boolean) : OneTapSignInResult()
    data class OneTapSuccess(val oneTapSuccess: Boolean) : OneTapSignInResult()
    data class Error(val message: String) : OneTapSignInResult()
}