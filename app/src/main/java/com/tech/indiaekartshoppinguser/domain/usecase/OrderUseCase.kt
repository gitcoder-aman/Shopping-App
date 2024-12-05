package com.tech.indiaekartshoppinguser.domain.usecase

import com.tech.indiaekartshoppinguser.domain.models.OrderModel
import com.tech.indiaekartshoppinguser.domain.models.ShippingModel
import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import javax.inject.Inject

class OrderUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) {
    fun invoke(orderList: List<OrderModel>) = shoppingRepository.orderDataSave(orderList)
    fun invoke() = shoppingRepository.getAllOrderData()
}