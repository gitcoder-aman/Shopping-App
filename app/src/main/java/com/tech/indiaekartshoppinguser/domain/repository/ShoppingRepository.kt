package com.tech.indiaekartshoppinguser.domain.repository

import android.app.Activity
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.tech.indiaekartshoppinguser.common.OneTapSignInResult
import com.tech.indiaekartshoppinguser.common.ResultState
import com.tech.indiaekartshoppinguser.domain.models.CartModel
import com.tech.indiaekartshoppinguser.domain.models.Category
import com.tech.indiaekartshoppinguser.domain.models.OrderModel
import com.tech.indiaekartshoppinguser.domain.models.ProductModel
import com.tech.indiaekartshoppinguser.domain.models.ShippingModel
import com.tech.indiaekartshoppinguser.domain.models.UserModel
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    fun getAllCategory() : Flow<ResultState<List<Category>>>
    fun getAllProducts() : Flow<ResultState<List<ProductModel>>>
    fun registerUserWithEmailPassword(userModel : UserModel) : Flow<ResultState<String>>
    fun loginUserWithEmailPassword(userModel : UserModel) : Flow<ResultState<String>>
    fun getProductById(productId : String) : Flow<ResultState<ProductModel>>
    fun getUserData(uuid: String): Flow<ResultState<UserModel>>
    fun updateUserData(userModel: UserModel) : Flow<ResultState<String>>
    fun uploadUserImage(imageUri : Uri) : Flow<ResultState<String>>
    fun productInFavouritePerform(productModel: ProductModel) : Flow<ResultState<String>>
    fun checkProductInFavouriteOrNot(productId: String) : Flow<ResultState<Boolean>>
    fun getAllFavouriteProducts() : Flow<ResultState<List<ProductModel>>>

    fun productInCartPerform(cartModel: CartModel) : Flow<ResultState<String>>
    fun checkProductInCartOrNot(productId: String) : Flow<ResultState<Boolean>>
    fun getAllCartProducts() : Flow<ResultState<List<CartModel>>>
    fun initiateOneTapSignIn(activity: Activity, signInLauncher: ActivityResultLauncher<IntentSenderRequest>) : Flow<OneTapSignInResult>
    fun firebaseWithGoogle(idToken: String) : Flow<OneTapSignInResult>
    fun shippingDataSave(shippingModel: ShippingModel) : Flow<ResultState<String>>
    fun shippingDataGetThroughUID() : Flow<ResultState<ShippingModel>>
    fun orderDataSave(orderList: List<OrderModel>) : Flow<ResultState<String>>
    fun deleteProductInCart() : Flow<ResultState<String>>
    fun getAllOrderData() : Flow<ResultState<List<OrderModel>>>
}