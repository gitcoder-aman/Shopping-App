package com.tech.indiaekartshoppinguser.domain.usecase

import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import javax.inject.Inject

class GetAllCategoryUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
){
    fun getAllCategory() = shoppingRepository.getAllCategory()

}