package com.tech.indiaekartshoppinguser.domain.models

data class OrderModel(
    val orderId : String = "",
    var productId : String = "",
    val productName : String = "",
    val productDescription : String = "",
    val productQty : String = "",
    val productPrice : String = "",
    val productFinalPrice : String = "",
    val productCategory : String = "",
    val productImageUrl : String = "",
    val color : String = "",
    val size : String = "",
    val date : Long = System.currentTimeMillis(),
    val totalPrice : String = "",
    val transactionMethod : String = "",
    val transactionId : String = "",
    val userAddress : String = "",
    val city : String = "",
    val countryRegion : String = "",
    val userEmail : String = "",
    val firstName : String = "",
    val lastName : String = "",
    val mobileNo : String = "",
    val postalCode : String = ""
)
