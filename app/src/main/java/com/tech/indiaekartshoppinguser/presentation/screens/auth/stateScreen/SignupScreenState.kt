package com.tech.indiaekartshoppinguser.presentation.screens.auth.stateScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class SignupScreenState(
    val firstName : MutableState<String> = mutableStateOf(""),
    val lastName : MutableState<String> = mutableStateOf(""),
    val email : MutableState<String> = mutableStateOf(""),
    val password : MutableState<String> = mutableStateOf(""),
    val confirmPassword : MutableState<String> = mutableStateOf(""),
    val phoneNumber : MutableState<String> = mutableStateOf(""),
    val address : MutableState<String> = mutableStateOf(""),
    val userImage : MutableState<String> = mutableStateOf(""),
    val uuid : MutableState<String> = mutableStateOf("")

)
