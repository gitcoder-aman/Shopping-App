package com.tech.indiaekartshoppinguser.presentation.screens.profile.stateScreen

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class ProfileUserDataScreenState(
    val firstName: MutableState<String> = mutableStateOf(""),
    val lastName: MutableState<String> = mutableStateOf(""),
    val email: MutableState<String> = mutableStateOf(""),
    val phoneNumber: MutableState<String> = mutableStateOf(""),
    val address: MutableState<String> = mutableStateOf(""),
    val password: MutableState<String> = mutableStateOf(""),
    val userImageUrl : MutableState<String> = mutableStateOf(""),
    val userImageUri : MutableState<Uri?> = mutableStateOf(null)
)
