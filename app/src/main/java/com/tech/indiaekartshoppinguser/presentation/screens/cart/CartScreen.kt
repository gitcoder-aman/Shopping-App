package com.tech.indiaekartshoppinguser.presentation.screens.cart

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.tech.indiaekartshoppinguser.presentation.screens.cart.components.ShoppingCartEachRow
import com.tech.indiaekartshoppinguser.presentation.screens.components.ButtonComponent
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ShoppingViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.DarkGrayColor
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor
import com.tech.indiaekartshoppinguser.utils.sumAllTotalPrice

@Composable
fun CartScreen(navController: NavHostController, shoppingViewmodel: ShoppingViewmodel) {

    LaunchedEffect(Unit) {
        shoppingViewmodel.getAllCartProducts()
    }
    val getAllCartProducts = shoppingViewmodel.getAllCartProducts.collectAsStateWithLifecycle()
    val productInCartPerform = shoppingViewmodel.productInCartPerformState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    if(productInCartPerform.value.isLoading){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MainColor)
        }
    }else if(productInCartPerform.value.message != null){
        shoppingViewmodel.getAllCartProducts()
        shoppingViewmodel.resetProductInCartState()
        Toast.makeText(context, productInCartPerform.value.message, Toast.LENGTH_SHORT).show()
    }else if(productInCartPerform.value.error.isNotEmpty()){
        shoppingViewmodel.getAllCartProducts()
        shoppingViewmodel.resetProductInCartState()
        Toast.makeText(context, productInCartPerform.value.error, Toast.LENGTH_SHORT).show()
    }
    if (getAllCartProducts.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MainColor)
        }
    } else if (getAllCartProducts.value.cartProductData.isNotEmpty()) {
        val listOfCart = getAllCartProducts.value.cartProductData
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteColor)
        ) {

            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ellipse_1),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopEnd)
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(), verticalArrangement = Arrangement.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(top = 70.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = BlackColor,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navController.navigateUp()
                            }
                    )
                    Text(
                        text = "Shopping Cart",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W600,
                            textAlign = TextAlign.Start,
                            color = BlackColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        ),
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "Items",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.W600,
                            textAlign = TextAlign.Start,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        ), modifier = Modifier.weight(0.8f)
                    )
                    Text(
                        text = "Price",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.W600,
                            textAlign = TextAlign.Start,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        ), modifier = Modifier.weight(0.3f)
                    )
                    Text(
                        text = "Qty",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.W600,
                            textAlign = TextAlign.Start,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        ), modifier = Modifier.weight(0.3f)
                    )
                    Text(
                        text = "Total",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.W600,
                            textAlign = TextAlign.Start,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        ), modifier = Modifier.weight(0.3f)
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    items(listOfCart) {
                        ShoppingCartEachRow(it){
                            shoppingViewmodel.productInCartPerform(it)
                        }
                    }
                    item {
                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = DarkGrayColor
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Sub Total",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.W600,
                                    textAlign = TextAlign.Start,
                                    color = BlackColor,
                                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                                )
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Rs: ${sumAllTotalPrice(cartModel = listOfCart)}",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W600,
                                    textAlign = TextAlign.Start,
                                    color = GrayColor,
                                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                                )
                            )
                        }

                        Spacer(Modifier.height(8.dp))
                        ButtonComponent(
                            text = "Checkout",
                            containerColor = GrayColor,
                            onClick = {
                                //here to move to shipping screen with cart all data
                                navController.navigate(Routes.ShippingScreen(
                                    productId = "",
                                    productQty = "",
                                    color = "",
                                    size = ""
                                ))
                            }
                        )
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Text(text = "No Data Found")
        }
    }
}
