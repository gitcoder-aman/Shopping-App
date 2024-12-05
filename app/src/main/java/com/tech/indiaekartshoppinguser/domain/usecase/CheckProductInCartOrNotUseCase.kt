package com.tech.indiaekartshoppinguser.domain.usecase

import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import javax.inject.Inject

class CheckProductInCartOrNotUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    fun invoke(productId: String) = shoppingRepository.checkProductInCartOrNot(productId)
}