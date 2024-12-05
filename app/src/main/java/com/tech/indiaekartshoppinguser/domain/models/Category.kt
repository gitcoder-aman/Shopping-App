package com.tech.indiaekartshoppinguser.domain.models

data class Category(
    var categoryId : String = "",
    var categoryName: String = "",
    val date: Long = System.currentTimeMillis(),
    var imageUrl: String = "",
)
