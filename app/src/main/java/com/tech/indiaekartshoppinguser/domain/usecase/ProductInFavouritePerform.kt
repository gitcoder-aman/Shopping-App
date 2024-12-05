package com.tech.indiaekartshoppinguser.domain.usecase

import com.tech.indiaekartshoppinguser.domain.models.ProductModel
import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import javax.inject.Inject

class ProductInFavouritePerform @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    fun invoke(productModel: ProductModel) = shoppingRepository.productInFavouritePerform(productModel)
}