package com.tech.indiaekartshoppinguser.domain.models

data class ProductModel(
    var productId : String = "",
    val productName : String = "",
    val productDescription : String = "",
    val productPrice : String = "",
    val productFinalPrice : String = "",
    val productCategory : String = "",
    val productImageUrl : String = "",
    val availableUnit : String = "",
    val available : String = "",
    val date : Long = System.currentTimeMillis()
)
fun ProductModel.toCartModel(
    productQty: String = "1", // Default quantity
    color: String = "Default Color", // Default color
    size: String = "Default Size" // Default size
): CartModel {
    return CartModel(
        productId = this.productId,
        productName = this.productName,
        productDescription = this.productDescription,
        productQty = productQty,
        productPrice = this.productPrice,
        productFinalPrice = this.productFinalPrice,
        productCategory = this.productCategory,
        productImageUrl = this.productImageUrl,
        color = color,
        size = size,
        date = System.currentTimeMillis()
    )
}

