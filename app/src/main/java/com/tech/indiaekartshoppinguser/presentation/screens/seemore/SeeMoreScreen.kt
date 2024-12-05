package com.tech.indiaekartshoppinguser.presentation.screens.seemore

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.presentation.navigation.Routes
import com.tech.indiaekartshoppinguser.presentation.screens.home.components.CategoryEachRow
import com.tech.indiaekartshoppinguser.presentation.screens.seemore.components.EachProductView
import com.tech.indiaekartshoppinguser.presentation.screens.seemore.components.SearchBarView
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ShoppingViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.DarkGrayColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun SeeMoreScreen(
    shoppingViewmodel: ShoppingViewmodel,
    navController: NavHostController,
    seeAllScreenName: String
) {

    val homeScreenState = shoppingViewmodel.homeScreenState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var searchTextValue by remember {
        mutableStateOf("")
    }
    val listOfProduct = if(searchTextValue.isNotEmpty()){
        homeScreenState.value.productsData.filter {
            it.productName.contains(searchTextValue, ignoreCase = true)
        }
    }else{
        homeScreenState.value.productsData
    }
    val listOfCategory = if(searchTextValue.isNotEmpty()){
        homeScreenState.value.categoriesData.filter {
            it.categoryName.contains(searchTextValue, ignoreCase = true)
        }
    }else{
        homeScreenState.value.categoriesData
    }
    when {
        homeScreenState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        homeScreenState.value.error.isNotEmpty() -> {
            Toast.makeText(context, homeScreenState.value.error, Toast.LENGTH_SHORT).show()
        }

        homeScreenState.value.productsData.isNotEmpty() && homeScreenState.value.categoriesData.isNotEmpty() -> {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WhiteColor)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize(), verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.padding(top = 100.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            tint = BlackColor,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(36.dp).clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    navController.navigateUp()
                                }
                        )
                        Text(
                            text = "See More",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.W600,
                                textAlign = TextAlign.Start,
                                color = BlackColor,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold))
                            ),
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.End
                    ) {
                        SearchBarView(
                            value = searchTextValue,
                            onValueChange = {
                                searchTextValue = it
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = DarkGrayColor
                        )
                        Spacer(Modifier.height(16.dp))
                        if(seeAllScreenName == "Categories"){
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(3),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(listOfCategory, key = {
                                    it.categoryId
                                }){
                                    CategoryEachRow(it)
                                }
                            }
                        }else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(listOfProduct, key = {
                                    it.productId
                                }) {
                                    EachProductView(it){
                                        navController.navigate(Routes.ProductDetailScreen(
                                            productId = it.productId
                                        ))
                                    }
                                }

                            }
                        }
                    }
                }
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ellipse_1),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.TopEnd)
                )

            }
        }
    }
}

