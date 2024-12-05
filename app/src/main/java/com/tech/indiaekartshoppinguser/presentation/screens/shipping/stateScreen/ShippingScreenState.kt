package com.tech.indiaekartshoppinguser.presentation.screens.shipping.stateScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class ShippingScreenState(
    val email : MutableState<String> = mutableStateOf(""),
    val mobileNo : MutableState<String> = mutableStateOf(""),
    val countryRegion : MutableState<String> = mutableStateOf(""),
    val firstName : MutableState<String> = mutableStateOf(""),
    val lastName : MutableState<String> = mutableStateOf(""),
    val address : MutableState<String> = mutableStateOf(""),
    val city : MutableState<String> = mutableStateOf(""),
    val postalCode : MutableState<String> = mutableStateOf(""),
    val isSaveForNextTime : MutableState<Boolean> = mutableStateOf(false)
)
