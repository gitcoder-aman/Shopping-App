package com.tech.indiaekartshoppinguser.domain.usecase

import com.tech.indiaekartshoppinguser.domain.models.CartModel
import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import javax.inject.Inject

class ProductInCartPerform @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    fun invoke(cartModel: CartModel) = shoppingRepository.productInCartPerform(cartModel)
    fun invoke() = shoppingRepository.deleteProductInCart()
}