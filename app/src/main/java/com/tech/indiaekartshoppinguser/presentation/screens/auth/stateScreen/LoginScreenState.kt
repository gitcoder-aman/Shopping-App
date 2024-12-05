package com.tech.indiaekartshoppinguser.presentation.screens.auth.stateScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class LoginScreenState(
    val email : MutableState<String> = mutableStateOf(""),
    val password : MutableState<String> = mutableStateOf(""),
)
