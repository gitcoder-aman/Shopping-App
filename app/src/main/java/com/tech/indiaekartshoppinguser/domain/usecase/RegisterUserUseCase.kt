package com.tech.indiaekartshoppinguser.domain.usecase

import com.tech.indiaekartshoppinguser.domain.models.UserModel
import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
){
    fun invoke(userModel: UserModel) = shoppingRepository.registerUserWithEmailPassword(userModel)

}