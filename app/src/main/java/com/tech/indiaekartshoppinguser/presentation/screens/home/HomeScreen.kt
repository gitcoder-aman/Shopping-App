package com.tech.indiaekartshoppinguser.presentation.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tech.indiaekartshoppinguser.presentation.navigation.Routes
import com.tech.indiaekartshoppinguser.presentation.screens.home.components.CategoryEachRow
import com.tech.indiaekartshoppinguser.presentation.screens.home.components.PagerSlider
import com.tech.indiaekartshoppinguser.presentation.screens.home.components.ProductEachRow
import com.tech.indiaekartshoppinguser.presentation.screens.home.components.SeeMore
import com.tech.indiaekartshoppinguser.presentation.screens.home.components.TopBar
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ShoppingViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun HomeScreen(
    navController: NavController,
    shoppingViewmodel: ShoppingViewmodel
) {
    val homeScreenState = shoppingViewmodel.homeScreenState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var searchTextValue by remember {
        mutableStateOf("")
    }

    val listOfProduct = if (searchTextValue.isNotEmpty()) {
        homeScreenState.value.productsData.filter {
            it.productName.contains(searchTextValue, ignoreCase = true)
        }
    } else {
        homeScreenState.value.productsData
    }
    when {
        homeScreenState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MainColor)
            }
        }

        homeScreenState.value.error.isNotEmpty() -> {
            Toast.makeText(context, homeScreenState.value.error, Toast.LENGTH_SHORT).show()
        }

        homeScreenState.value.productsData.isNotEmpty() && homeScreenState.value.categoriesData.isNotEmpty() -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
                    .background(WhiteColor)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                TopBar(
                    value = searchTextValue,
                    onNotificationClick = {
                        navController.navigate(Routes.NotificationScreen)
                    }
                ) {
                    searchTextValue = it
                }
                Spacer(Modifier.height(8.dp))
                SeeMore(
                    text1 = "Categories",
                    text2 = "see more"
                ) {
                    navController.navigate(
                        Routes.SeeAllScreen(
                            seeAllScreenName = "Categories"
                        )
                    )
                }
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(homeScreenState.value.categoriesData, key = {
                        it.categoryId
                    }) {
                        CategoryEachRow(
                            it
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                PagerSlider()
                Spacer(Modifier.height(8.dp))
                SeeMore(
                    text1 = "Flash Sale",
                    text2 = "see more"
                ) {
                    navController.navigate(
                        Routes.SeeAllScreen(
                            seeAllScreenName = "Flash Sale"
                        )
                    )
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOfProduct, key = {
                        it.productId
                    }) {
                        ProductEachRow(it) {
                            navController.navigate(
                                Routes.ProductDetailScreen(
                                    productId = it.productId
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}