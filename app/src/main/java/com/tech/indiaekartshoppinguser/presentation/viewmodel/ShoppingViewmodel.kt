package com.tech.indiaekartshoppinguser.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.indiaekartshoppinguser.common.ResultState
import com.tech.indiaekartshoppinguser.domain.models.CartModel
import com.tech.indiaekartshoppinguser.domain.models.Category
import com.tech.indiaekartshoppinguser.domain.models.ProductModel
import com.tech.indiaekartshoppinguser.domain.usecase.CheckProductInCartOrNotUseCase
import com.tech.indiaekartshoppinguser.domain.usecase.CheckProductInFavouriteOrNotUseCase
import com.tech.indiaekartshoppinguser.domain.usecase.GetAllCartProductsUseCase
import com.tech.indiaekartshoppinguser.domain.usecase.ProductInFavouritePerform
import com.tech.indiaekartshoppinguser.domain.usecase.GetAllCategoryUseCase
import com.tech.indiaekartshoppinguser.domain.usecase.GetAllFavouriteProductsUseCase
import com.tech.indiaekartshoppinguser.domain.usecase.GetAllProductsUseCase
import com.tech.indiaekartshoppinguser.domain.usecase.GetProductsByIdUseCase
import com.tech.indiaekartshoppinguser.domain.usecase.ProductInCartPerform
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewmodel @Inject constructor(
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getProductsByIdUseCase: GetProductsByIdUseCase,
    private val productInFavouritePerform: ProductInFavouritePerform,
    private val checkProductInFavouriteOrNotUseCase: CheckProductInFavouriteOrNotUseCase,
    private val getAllFavouriteProductsUseCase: GetAllFavouriteProductsUseCase,
    private val productInCartPerformUserCase: ProductInCartPerform,
    private val checkProductInCartOrNotUseCase: CheckProductInCartOrNotUseCase,
    private val getAllCartProductsUseCase: GetAllCartProductsUseCase
) : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    private val _getProductByIdState = MutableStateFlow(GetProductByIdState())
    val getProductByIdState = _getProductByIdState.asStateFlow()

    private val _productInFavouritePerformState = MutableStateFlow(ProductInFavouriteState())
    val productInFavouritePerformState = _productInFavouritePerformState.asStateFlow()

    private val _checkProductInFavouriteOrNotState =
        MutableStateFlow(CheckProductInFavouriteOrNotState())
    val checkProductInFavouriteOrNotState = _checkProductInFavouriteOrNotState.asStateFlow()

    private val _getAllFavouriteProducts = MutableStateFlow(GetAllFavouriteProduct())
    val getAllFavouriteProducts = _getAllFavouriteProducts.asStateFlow()

    private val _productInCartPerformState = MutableStateFlow(ProductInCartState())
    val productInCartPerformState = _productInCartPerformState.asStateFlow()

    private val _checkProductInCartOrNotState = MutableStateFlow(CheckProductInCartOrNotState())
    val checkProductInCartOrNotState = _checkProductInCartOrNotState.asStateFlow()

    private val _getAllCartProducts = MutableStateFlow(GetAllCartProductState())
    val getAllCartProducts = _getAllCartProducts.asStateFlow()

    private val _productInCartDeleteState = MutableStateFlow(ProductInCartState())
    val productInCartDeleteState = _productInCartDeleteState.asStateFlow()

    init {
        loadHomeScreenData()
    }

    private fun loadHomeScreenData() {
        viewModelScope.launch(Dispatchers.IO) {
            combine(
                getAllCategoryUseCase.getAllCategory(),
                getAllProductsUseCase.getAllProducts()
            ) { categoryState, productState ->

                // Combine the results of the two state flows
                when {
                    // If either category or product is loading, show loading state
                    categoryState is ResultState.Loading || productState is ResultState.Loading -> {
                        HomeScreenState(isLoading = true) // Only return the loading state
                    }

                    // If both are successful, update with both category and product data
                    categoryState is ResultState.Success && productState is ResultState.Success -> {
                        HomeScreenState(
                            isLoading = false,
                            categoriesData = categoryState.data,
                            productsData = productState.data
                        )
                    }

                    // If category state has an error, show that error
                    categoryState is ResultState.Error -> {
                        HomeScreenState(error = categoryState.error)
                    }

                    // If product state has an error, show that error
                    productState is ResultState.Error -> {
                        HomeScreenState(error = productState.error)
                    }

                    else -> {
                        HomeScreenState() // Default fallback state if no condition is met
                    }
                }
            }.collect { newState ->
                // Update the state flow with the new state from combine
                _homeScreenState.value = newState
            }
        }
    }

    fun getProductById(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getProductsByIdUseCase.invoke(productId).collect { productState ->
                when (productState) {
                    is ResultState.Loading -> {
                        _getProductByIdState.value = GetProductByIdState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getProductByIdState.value =
                            GetProductByIdState(productData = productState.data)
                    }

                    is ResultState.Error -> {
                        _getProductByIdState.value = GetProductByIdState(error = productState.error)
                    }
                }
            }
        }
    }

    fun productInFavouritePerform(productModel: ProductModel) {
        viewModelScope.launch(Dispatchers.IO) {
            productInFavouritePerform.invoke(productModel).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _productInFavouritePerformState.value =
                            ProductInFavouriteState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _productInFavouritePerformState.value =
                            ProductInFavouriteState(message = it.data)
                    }

                    is ResultState.Error -> {
                        _productInFavouritePerformState.value =
                            ProductInFavouriteState(error = it.error)
                    }
                }
            }
        }
    }

    fun checkProductInFavouriteOrNot(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            checkProductInFavouriteOrNotUseCase.invoke(productId).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _checkProductInFavouriteOrNotState.value =
                            CheckProductInFavouriteOrNotState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _checkProductInFavouriteOrNotState.value =
                            CheckProductInFavouriteOrNotState(isProductInFavourite = it.data)
                        Log.d(
                            "@viewmodel",
                            "ProductDetailScreen: ${_checkProductInFavouriteOrNotState.value.isProductInFavourite}"
                        )
                    }

                    is ResultState.Error -> {
                        _checkProductInFavouriteOrNotState.value =
                            CheckProductInFavouriteOrNotState(error = it.error)
                    }
                }
            }
        }
    }

    fun getAllFavouriteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllFavouriteProductsUseCase.getAllFavouriteProducts().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllFavouriteProducts.value = GetAllFavouriteProduct(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getAllFavouriteProducts.value =
                            GetAllFavouriteProduct(productsData = it.data)
                    }

                    is ResultState.Error -> {
                        _getAllFavouriteProducts.value = GetAllFavouriteProduct(error = it.error)
                    }
                }

            }

        }
    }

    fun productInCartPerform(cartModel: CartModel) {
        viewModelScope.launch(Dispatchers.IO) {
            productInCartPerformUserCase.invoke(cartModel).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _productInCartPerformState.value = ProductInCartState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _productInCartPerformState.value = ProductInCartState(message = it.data)
                    }

                    is ResultState.Error -> {
                        _productInCartPerformState.value = ProductInCartState(error = it.error)
                    }
                }
            }
        }
    }
    fun deleteProductInCart() {
        viewModelScope.launch(Dispatchers.IO) {
            productInCartPerformUserCase.invoke().collect{
                when (it) {
                    is ResultState.Loading -> {
                        _productInCartDeleteState.value = ProductInCartState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _productInCartDeleteState.value = ProductInCartState(message = it.data)
                    }
                    is ResultState.Error -> {
                        _productInCartDeleteState.value = ProductInCartState(error = it.error)
                    }
                }
            }
        }
    }

    fun checkProductInCartOrNot(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            checkProductInCartOrNotUseCase.invoke(productId).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _checkProductInCartOrNotState.value =
                            CheckProductInCartOrNotState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _checkProductInCartOrNotState.value =
                            CheckProductInCartOrNotState(isProductInCart = it.data)
                    }

                    is ResultState.Error -> {
                        _checkProductInCartOrNotState.value =
                            CheckProductInCartOrNotState(error = it.error)
                    }
                }
            }
        }
    }

    fun getAllCartProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllCartProductsUseCase.getAllCartProducts().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllCartProducts.value = GetAllCartProductState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getAllCartProducts.value =
                            GetAllCartProductState(cartProductData = it.data)
                    }

                    is ResultState.Error -> {
                        _getAllCartProducts.value = GetAllCartProductState(error = it.error)
                    }
                }

            }

        }
    }

    fun resetProductInFavouriteState() {
        _productInFavouritePerformState.value = ProductInFavouriteState()
        _checkProductInFavouriteOrNotState.value = CheckProductInFavouriteOrNotState()
        _getAllFavouriteProducts.value = GetAllFavouriteProduct()
    }

    fun resetProductInCartState() {
        _productInCartPerformState.value = ProductInCartState()
        _checkProductInCartOrNotState.value = CheckProductInCartOrNotState()
        _getAllCartProducts.value = GetAllCartProductState()
    }

}

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String = "",
    val categoriesData: List<Category> = emptyList(),
    val productsData: List<ProductModel> = emptyList()
)

data class GetProductByIdState(
    val isLoading: Boolean = false,
    val error: String = "",
    val productData: ProductModel? = null
)

data class ProductInFavouriteState(
    val isLoading: Boolean = false,
    val error: String = "",
    val message: String? = null
)

data class CheckProductInFavouriteOrNotState(
    val isLoading: Boolean = false,
    val error: String = "",
    val isProductInFavourite: Boolean = false
)

data class GetAllFavouriteProduct(
    val isLoading: Boolean = false,
    val error: String = "",
    val productsData: List<ProductModel> = emptyList()
)

data class ProductInCartState(
    val isLoading: Boolean = false,
    val error: String = "",
    val message: String? = null
)

data class CheckProductInCartOrNotState(
    val isLoading: Boolean = false,
    val error: String = "",
    val isProductInCart: Boolean = false
)

data class GetAllCartProductState(
    val isLoading: Boolean = false,
    val error: String = "",
    val cartProductData: List<CartModel> = emptyList()
)