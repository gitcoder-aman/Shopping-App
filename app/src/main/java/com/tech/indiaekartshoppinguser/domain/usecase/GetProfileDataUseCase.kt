package com.tech.indiaekartshoppinguser.domain.usecase

import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import javax.inject.Inject

class GetProfileDataUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
){
    fun invoke(uuid : String) = shoppingRepository.getUserData(uuid)

}