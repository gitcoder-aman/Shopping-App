package com.tech.indiaekartshoppinguser.domain.models

data class UserModel(
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val password : String = "",
    val phoneNumber : String = "",
    val address : String = "",
    val userImage : String = "",
    var uuid : String = ""
)
