package com.tech.indiaekartshoppinguser.presentation.viewmodel

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.indiaekartshoppinguser.common.OneTapSignInResult
import com.tech.indiaekartshoppinguser.common.ResultState
import com.tech.indiaekartshoppinguser.domain.models.UserModel
import com.tech.indiaekartshoppinguser.domain.usecase.AuthWithGoogleUseCase
import com.tech.indiaekartshoppinguser.domain.usecase.LoginUserUseCase
import com.tech.indiaekartshoppinguser.domain.usecase.RegisterUserUseCase
import com.tech.indiaekartshoppinguser.presentation.screens.auth.stateScreen.LoginScreenState
import com.tech.indiaekartshoppinguser.presentation.screens.auth.stateScreen.SignupScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val authWithGoogleUseCase: AuthWithGoogleUseCase
) : ViewModel() {

    private val _userRegisterState = MutableStateFlow(UserSignUpState())
    val userRegisterState = _userRegisterState.asStateFlow()

    private val _userRegisterScreenState = MutableStateFlow(SignupScreenState())
    val userRegisterScreenState = _userRegisterScreenState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SignupScreenState()
    )

    private val _userLoginState = MutableStateFlow(UserLoginState())
    val userLoginState = _userLoginState.asStateFlow()

    private val _userLoginScreenState = MutableStateFlow(LoginScreenState())
    val userLoginScreenState = _userLoginScreenState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = LoginScreenState()
    )
    private val _userAuthWithGoogleState = MutableStateFlow(UserAuthWithGoogleState())
    val userAuthWithGoogleState = _userAuthWithGoogleState.asStateFlow()

     fun registerUserWithEmailPassword(userModel: UserModel) {
        viewModelScope.launch {
            registerUserUseCase.invoke(userModel).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _userRegisterState.value = UserSignUpState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _userRegisterState.value = UserSignUpState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _userRegisterState.value = UserSignUpState(error = it.error)
                    }
                }
            }
        }
    }

    fun loginUserWithEmailPassword(userModel: UserModel) {
        viewModelScope.launch {
            loginUserUseCase.invoke(userModel).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _userLoginState.value = UserLoginState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _userLoginState.value = UserLoginState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _userLoginState.value = UserLoginState(error = it.error)
                    }
                }
            }
        }

    }

    fun initiateOneTapGoogle(activity: Activity, signInLauncher : ActivityResultLauncher<IntentSenderRequest>){
        viewModelScope.launch {
            Log.d("GoogleOneTap", "initiateOneTapGoogle Activity: $activity")
            Log.d("GoogleOneTap", "initiateOneTapGoogle signInLauncher: $signInLauncher")
            authWithGoogleUseCase.oneTapSignIn(activity,signInLauncher).collect{
                when(it){
                    is OneTapSignInResult.Loading -> {
                        _userAuthWithGoogleState.value = UserAuthWithGoogleState(isLoading = true)
                    }
                    is OneTapSignInResult.OneTapSuccess -> {
                        _userAuthWithGoogleState.value = UserAuthWithGoogleState(onTapSuccess = true)
                    }
                    is OneTapSignInResult.Error -> {
                        _userAuthWithGoogleState.value = UserAuthWithGoogleState(error = it.message)
                    }
                    is OneTapSignInResult.Idle -> {
                        _userAuthWithGoogleState.value = UserAuthWithGoogleState(error = "Cancelled")
                    }
                    is OneTapSignInResult.FirebaseSuccess -> {

                    }
                }
            }
        }
    }
    fun firebaseWithGoogle(idToken : String){
        viewModelScope.launch {
            authWithGoogleUseCase.googleFirebaseLogin(idToken).collect{
                when(it){
                    is OneTapSignInResult.Loading -> {
                        _userAuthWithGoogleState.value = UserAuthWithGoogleState(isLoading = true)
                    }
                    is OneTapSignInResult.FirebaseSuccess -> {
                        _userAuthWithGoogleState.value = UserAuthWithGoogleState(isFirebaseLogin = true)
                    }
                    is OneTapSignInResult.Error -> {
                        _userAuthWithGoogleState.value = UserAuthWithGoogleState(error = it.message)
                    }
                    is OneTapSignInResult.Idle -> {
                        _userAuthWithGoogleState.value = UserAuthWithGoogleState(error = "Cancelled")
                    }

                    is OneTapSignInResult.OneTapSuccess -> {

                    }
                }
            }
        }
    }

    fun clearAuthUserState() {
        _userRegisterState.value = UserSignUpState()
        _userLoginState.value = UserLoginState()
        _userRegisterScreenState.value = SignupScreenState()
        _userLoginScreenState.value = LoginScreenState()
        _userAuthWithGoogleState.value = UserAuthWithGoogleState()
    }
}

data class UserSignUpState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: String? = null
)
data class UserLoginState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: String? = null
)
data class UserAuthWithGoogleState(
    val isLoading: Boolean = false,
    val error: String = "",
    val isFirebaseLogin : Boolean = false,
    val onTapSuccess : Boolean = false
)