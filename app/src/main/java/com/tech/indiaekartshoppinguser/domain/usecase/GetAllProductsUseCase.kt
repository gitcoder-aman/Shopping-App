package com.tech.indiaekartshoppinguser.domain.usecase

import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
){
    fun getAllProducts() = shoppingRepository.getAllProducts()

}