package com.tech.indiaekartshoppinguser.presentation.navigation.bottomNavigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.tech.indiaekartshoppinguser.presentation.navigation.Routes
import com.tech.indiaekartshoppinguser.presentation.navigation.SubNavigation
import com.tech.indiaekartshoppinguser.presentation.screens.cart.CartScreen
import com.tech.indiaekartshoppinguser.presentation.screens.home.HomeScreen
import com.tech.indiaekartshoppinguser.presentation.screens.auth.LoginScreen
import com.tech.indiaekartshoppinguser.presentation.screens.profile.ProfileScreen
import com.tech.indiaekartshoppinguser.presentation.screens.auth.SignUpScreen
import com.tech.indiaekartshoppinguser.presentation.screens.notification.NotificationScreen
import com.tech.indiaekartshoppinguser.presentation.screens.order.OrderScreen
import com.tech.indiaekartshoppinguser.presentation.screens.payment.PaymentScreen
import com.tech.indiaekartshoppinguser.presentation.screens.payment.SuccessFulPurchaseScreen
import com.tech.indiaekartshoppinguser.presentation.screens.payment.noification.NotificationPermission
import com.tech.indiaekartshoppinguser.presentation.screens.product.ProductDetailScreen
import com.tech.indiaekartshoppinguser.presentation.screens.seemore.SeeMoreScreen
import com.tech.indiaekartshoppinguser.presentation.screens.shipping.ShippingScreen
import com.tech.indiaekartshoppinguser.presentation.screens.wishlist.WishListScreen
import com.tech.indiaekartshoppinguser.presentation.viewmodel.AuthViewmodel
import com.tech.indiaekartshoppinguser.presentation.viewmodel.OrderViewmodel
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ProfileViewmodel
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ShippingViewmodel
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ShoppingViewmodel

@Composable
fun ShoppingNavigator(
    authViewmodel: AuthViewmodel,
    firebaseAuth: FirebaseAuth,
    onGoogleSignInClick: () -> Unit,
    navController: NavHostController
) {
    val shoppingViewmodel = hiltViewModel<ShoppingViewmodel>()
    val profileViewmodel = hiltViewModel<ProfileViewmodel>()
    val shippingViewmodel = hiltViewModel<ShippingViewmodel>()
    val orderViewmodel = hiltViewModel<OrderViewmodel>()


    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            text = "Home",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            text = "WishList",
            selectedIcon = Icons.Filled.Favorite,
            unSelectedIcon = Icons.Outlined.FavoriteBorder
        ),
        BottomNavigationItem(
            text = "Cart",
            selectedIcon = Icons.Filled.ShoppingCart,
            unSelectedIcon = Icons.Outlined.ShoppingCart
        ),
        BottomNavigationItem(
            text = "Profile",
            selectedIcon = Icons.Filled.Person,
            unSelectedIcon = Icons.Outlined.Person
        ),

        )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    val backStackEntry = navController.currentBackStackEntryAsState()


    // Update selectedItem based on backStackEntry
    backStackEntry.value?.let { entry ->
        selectedItemIndex = when (entry.destination.route) {
            Routes.HomeScreen::class.qualifiedName -> 0
            Routes.WishListScreen::class.qualifiedName -> 1
            Routes.CartScreen::class.qualifiedName -> 2
            Routes.ProfileScreen::class.qualifiedName -> 3
            else -> 0
        }
    }
    val isBottomBarVisible = backStackEntry.value?.destination?.route in listOf(
        Routes.HomeScreen::class.qualifiedName,
        Routes.WishListScreen::class.qualifiedName,
        Routes.CartScreen::class.qualifiedName,
        Routes.ProfileScreen::class.qualifiedName
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                BottomNavigation(
                    items = bottomNavigationItems,
                    selectedItemIndex = selectedItemIndex,
                    onItemClick = { index ->
                        selectedItemIndex = index

                        when (selectedItemIndex) {
                            0 -> {
                                navController.navigate(Routes.HomeScreen) {
                                    launchSingleTop = true
                                    popUpTo(Routes.HomeScreen) {
                                        inclusive = true
                                    }
                                }
                            }

                            1 -> {
                                navController.navigate(Routes.WishListScreen) {
                                    launchSingleTop = true
                                    popUpTo(Routes.WishListScreen) {
                                        inclusive = true
                                    }
                                }
                            }

                            2 -> {
                                navController.navigate(Routes.CartScreen) {
                                    launchSingleTop = true
                                    popUpTo(Routes.CartScreen) {
                                        inclusive = true
                                    }
                                }
                            }

                            3 -> {
                                navController.navigate(Routes.ProfileScreen) {
                                    launchSingleTop = true
                                    popUpTo(Routes.ProfileScreen) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        val startDestination = if (firebaseAuth.currentUser != null) {
            SubNavigation.MainHomeScreen
        } else {
            SubNavigation.LoginSignUpScreen
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                navigation<SubNavigation.LoginSignUpScreen>(startDestination = Routes.LoginScreen) {
                    composable<Routes.LoginScreen> {
                        LoginScreen(navController, authViewmodel, onSignInClick = {
                            onGoogleSignInClick()
                        })
                    }
                    composable<Routes.SignUpScreen> {
                        SignUpScreen(navController, authViewmodel, onSignInClick = {
                            onGoogleSignInClick()
                        })
                    }
                }
                navigation<SubNavigation.MainHomeScreen>(startDestination = Routes.HomeScreen) {
                    composable<Routes.HomeScreen> {
                        NotificationPermission()
                        HomeScreen(navController = navController, shoppingViewmodel)
                    }
                    composable<Routes.WishListScreen> {
                        WishListScreen(navController, shoppingViewmodel)
                    }
                    composable<Routes.CartScreen> {
                        CartScreen(navController, shoppingViewmodel)
                    }
                    composable<Routes.ProfileScreen> {
                        ProfileScreen(
                            navController, profileViewmodel,
                            firebaseAuth
                        )
                    }
                }
                composable<Routes.ProductDetailScreen> { backStackEntry ->
                    ProductDetailScreen(
                        productId = backStackEntry.toRoute<Routes.ProductDetailScreen>().productId,
                        shoppingViewmodel = shoppingViewmodel,
                        navController = navController
                    )
                }
                composable<Routes.SeeAllScreen> { backStackEntry ->
                    val seeAllScreenName =
                        backStackEntry.toRoute<Routes.SeeAllScreen>().seeAllScreenName
                    SeeMoreScreen(shoppingViewmodel, navController, seeAllScreenName)
                }
                composable<Routes.NotificationScreen> {
                    NotificationScreen(navController)
                }
                composable<Routes.ShippingScreen> { backStackEntry ->
                    val productId = backStackEntry.toRoute<Routes.ShippingScreen>().productId
                    val color = backStackEntry.toRoute<Routes.ShippingScreen>().color
                    val size = backStackEntry.toRoute<Routes.ShippingScreen>().size
                    val productQty = backStackEntry.toRoute<Routes.ShippingScreen>().productQty

                    ShippingScreen(
                        navController,
                        shippingViewmodel,
                        shoppingViewmodel,
                        productId,
                        color,
                        size,
                        productQty
                    )
                }
                composable<Routes.PaymentScreen> {
                    PaymentScreen(
                        navController,
                        shoppingViewmodel,
                        orderViewmodel,
                        shippingViewmodel
                    )
                }
                composable<Routes.SuccessfulPurchaseScreen> {
                    SuccessFulPurchaseScreen(navController)
                }
                composable<Routes.OrderScreen> {
                    OrderScreen(navController,orderViewmodel)
                }

            }
        }

    }

}