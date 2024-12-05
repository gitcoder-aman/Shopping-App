package com.tech.indiaekartshoppinguser.domain.usecase

import com.tech.indiaekartshoppinguser.domain.models.ShippingModel
import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import javax.inject.Inject

class ShippingUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    fun invoke(shippingModel: ShippingModel) = shoppingRepository.shippingDataSave(shippingModel)
    fun invoke() = shoppingRepository.shippingDataGetThroughUID()
}