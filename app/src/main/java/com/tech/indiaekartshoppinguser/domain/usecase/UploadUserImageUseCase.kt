package com.tech.indiaekartshoppinguser.domain.usecase

import android.net.Uri
import com.tech.indiaekartshoppinguser.domain.repository.ShoppingRepository
import javax.inject.Inject

class UploadUserImageUseCase @Inject constructor(
    private val shoppingRepository: ShoppingRepository
){
    fun invoke(imageUri : Uri) = shoppingRepository.uploadUserImage(imageUri)

}