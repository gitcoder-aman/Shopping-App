package com.tech.indiaekartshoppinguser.presentation.navigation

import kotlinx.serialization.Serializable


sealed class SubNavigation{
    @Serializable
    object MainHomeScreen : SubNavigation()

    @Serializable
    object LoginSignUpScreen : SubNavigation()

}
sealed class Routes {
    @Serializable
    object LoginScreen

    @Serializable
    object SignUpScreen

    @Serializable
    object HomeScreen

    @Serializable
    object ProfileScreen

    @Serializable
    object WishListScreen

    @Serializable
    object CartScreen

    @Serializable
    data class ShippingScreen(
        val productId: String = "",
        val productQty : String = "",
        val color : String = "",
        val size : String = ""
    )

    @Serializable
    object PaymentScreen

    @Serializable
    object NotificationScreen

    @Serializable
    data class SeeAllScreen(
        val seeAllScreenName : String
    )

    @Serializable
    data class ProductDetailScreen(
        val productId: String
    )

    @Serializable
    object SuccessfulPurchaseScreen

    @Serializable
    object AllCategoryScreen

    @Serializable
    object OrderScreen

}