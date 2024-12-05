package com.tech.indiaekartshoppinguser.presentation.screens.payment

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tech.indiaekartshoppinguser.MainActivity
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.domain.models.CartModel
import com.tech.indiaekartshoppinguser.domain.models.OrderModel
import com.tech.indiaekartshoppinguser.presentation.navigation.Routes
import com.tech.indiaekartshoppinguser.presentation.screens.components.ButtonComponent
import com.tech.indiaekartshoppinguser.presentation.screens.payment.noification.createNotificationChannel
import com.tech.indiaekartshoppinguser.presentation.screens.payment.noification.sendNotification
import com.tech.indiaekartshoppinguser.presentation.screens.shipping.ShippingProductColumn
import com.tech.indiaekartshoppinguser.presentation.viewmodel.GetShippingDataState
import com.tech.indiaekartshoppinguser.presentation.viewmodel.OrderViewmodel
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ShippingViewmodel
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ShoppingViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.CardBackGroundColor
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor
import com.tech.indiaekartshoppinguser.utils.sumAllTotalPrice

@Composable
fun PaymentScreen(
    navController: NavController,
    shoppingViewmodel: ShoppingViewmodel,
    orderViewmodel: OrderViewmodel,
    shippingViewmodel: ShippingViewmodel
) {

    val getAllCartProducts = shoppingViewmodel.getAllCartProducts.collectAsStateWithLifecycle()
    val orderState = orderViewmodel.orderState.collectAsStateWithLifecycle()
    val shippingDataState = shippingViewmodel.getShippingState.collectAsStateWithLifecycle()
    val productInCartDeleteState =
        shoppingViewmodel.productInCartDeleteState.collectAsStateWithLifecycle() //delete
    val paymentStatus = orderViewmodel.paymentState.collectAsStateWithLifecycle()

    val cartProductList = getAllCartProducts.value.cartProductData

    LaunchedEffect(Unit) {
        shippingViewmodel.getShippingDataThroughUID()
    }
    val context = LocalContext.current
    val cntx = LocalContext.current as MainActivity

    var isRadioBankCardSelected by rememberSaveable {
        mutableStateOf(false)
    }
    var isRadioCashSelected by rememberSaveable {
        mutableStateOf(false)
    }
    var isRadioShippingAddressSelected by rememberSaveable {
        mutableStateOf(false)
    }
    var isRadioDifferentAddressSelected by rememberSaveable {
        mutableStateOf(false)
    }

    if (getAllCartProducts.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MainColor)
        }
    } else if (getAllCartProducts.value.error.isNotEmpty()) {
        Toast.makeText(context, getAllCartProducts.value.error, Toast.LENGTH_SHORT).show()
    } else {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(WhiteColor)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Payments", style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W600,
                    color = BlackColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
            )
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null
                ) {
                    navController.navigateUp()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = GrayColor
                )
                Text(
                    text = "Return to Shipping", style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600,
                        color = GrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                )
            }
            Spacer(Modifier.height(4.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                items(cartProductList) { cartProduct ->
                    if (cartProductList.size > 1) {
                        ShippingProductColumn(
                            cartProduct = cartProduct,
                            modifier = Modifier.width(300.dp)
                        )
                    } else {
                        ShippingProductColumn(
                            cartProduct = cartProduct,
                            modifier = Modifier.width(350.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 0.5.dp,
                color = GrayColor,
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total", style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W700,
                        color = GrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                )
                Text(
                    text = "Rs: " + sumAllTotalPrice(cartProductList), style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W700,
                        color = GrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                )
            }
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = GrayColor,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Shipping Method", style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700,
                    color = GrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "All transactions are secure and encrypted.", style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W400,
                    color = GrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                )
            )
            Spacer(Modifier.height(8.dp))
            PaymentCard(
                isRadioBankCardSelected = isRadioBankCardSelected,
                isRadioCashSelected = isRadioCashSelected,
                onRadioBankCardSelected = {
                    isRadioBankCardSelected = !isRadioBankCardSelected
                    if (isRadioBankCardSelected) {
                        isRadioCashSelected = false
                    }
                },
                onRadioCashSelected = {
                    isRadioCashSelected = !isRadioCashSelected
                    if (isRadioCashSelected) {
                        isRadioBankCardSelected = false
                    }
                }
            )
            Spacer(Modifier.height(8.dp))
            BillingAddressColumn(
                isRadioShippingAddressSelected = isRadioShippingAddressSelected,
                isRadioDifferentAddressSelected = isRadioDifferentAddressSelected,
                onRadioShippingAddressSelected = {
                    isRadioShippingAddressSelected = !isRadioShippingAddressSelected
                    if (isRadioShippingAddressSelected) {
                        isRadioDifferentAddressSelected = false
                    }
                },
                onRadioDifferentAddressSelected = {
                    isRadioDifferentAddressSelected = !isRadioDifferentAddressSelected
                    if (isRadioDifferentAddressSelected) {
                        isRadioShippingAddressSelected = false
                    }
                }
            )
            Spacer(Modifier.height(8.dp))
            ButtonComponent(
                text = "Pay now",
                containerColor = GrayColor,
            ) {
                if (isRadioBankCardSelected) {
                    cntx.initPayment(payableAmount = sumAllTotalPrice(cartProductList).toInt() * 100)
                } else if (isRadioCashSelected) {
                    orderCreate(
                        shippingDataState,
                        cartProductList,
                        orderViewmodel,
                        paymentStatus.value.paymentState
                    )
                    createNotificationChannel(context)
                    sendNotification(context)
                }
            }
        }
    }
    when {
        paymentStatus.value.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Log.d("@pay", "PaymentScreen Loading...")
                CircularProgressIndicator(color = MainColor)
            }
        }

        paymentStatus.value.paymentState.isNotEmpty() -> {
            Log.d("@pay", "PaymentScreen data: ${paymentStatus.value.paymentState}")
            orderCreate(
                shippingDataState = shippingDataState,
                cartProductList = cartProductList,
                orderViewmodel = orderViewmodel,
                transactionId = paymentStatus.value.paymentState,
                transactionMethod = "Online Payment"
            )
            createNotificationChannel(context)
            sendNotification(context)
            orderViewmodel.clearPaymentState()
        }

        paymentStatus.value.error.isNotEmpty() -> {
            Log.d("@pay", "PaymentScreen error: ${paymentStatus.value.error}")
            Toast.makeText(context, paymentStatus.value.error, Toast.LENGTH_SHORT).show()
            orderViewmodel.clearPaymentState()
        }
    }
    when {
        orderState.value.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Log.d("@pay", "PaymentScreen Loading...")
                CircularProgressIndicator(color = MainColor)
            }
        }

        orderState.value.error.isNotEmpty() -> {
            Log.d("@pay", "PaymentScreen error: ${orderState.value.error}")
            Toast.makeText(context, orderState.value.error, Toast.LENGTH_SHORT).show()
        }

        orderState.value.data.isNotEmpty() -> {
            Log.d("@pay", "PaymentScreen data: ${orderState.value.data}")
            Toast.makeText(context, orderState.value.data, Toast.LENGTH_SHORT).show()
            orderViewmodel.clearOrderState()
            shoppingViewmodel.deleteProductInCart()
        }
    }
    if (productInCartDeleteState.value.message != null) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.SuccessfulPurchaseScreen)
        }
        Log.d("@pay", "PaymentScreen Cart : ${productInCartDeleteState.value.message}")
    } else if (productInCartDeleteState.value.error.isNotEmpty()) {
        Toast.makeText(context, productInCartDeleteState.value.error, Toast.LENGTH_SHORT).show()
        Log.d("@pay", "PaymentScreen Cart : ${productInCartDeleteState.value.message}")
    } else if (productInCartDeleteState.value.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MainColor)
        }
    }
}

fun orderCreate(
    shippingDataState: State<GetShippingDataState>,
    cartProductList: List<CartModel>,
    orderViewmodel: OrderViewmodel,
    transactionId: String = "Cash On Delivery",
    transactionMethod: String = "Cash On Delivery"
) {
    val orderList = mutableListOf<OrderModel>()
    if (shippingDataState.value.data != null) {
        val shippingData = shippingDataState.value.data!!
        for (cartProduct in cartProductList) {
            val orderModel = OrderModel(
                orderId = "O_" + System.currentTimeMillis(),
                productId = cartProduct.productId,
                productName = cartProduct.productName,
                productDescription = cartProduct.productDescription,
                productQty = cartProduct.productQty,
                productFinalPrice = cartProduct.productFinalPrice,
                productImageUrl = cartProduct.productImageUrl,
                color = cartProduct.color,
                size = cartProduct.size,
                productPrice = cartProduct.productPrice,
                totalPrice = ((cartProduct.productFinalPrice.toInt() * cartProduct.productQty.toInt()).toString()),
                userAddress = shippingData.address,
                userEmail = shippingData.email,
                firstName = shippingData.firstName,
                lastName = shippingData.lastName,
                postalCode = shippingData.postalCode,
                productCategory = cartProduct.productCategory,
                transactionMethod = transactionMethod,
                city = shippingData.city,
                countryRegion = shippingData.countryRegion,
                mobileNo = shippingData.mobileNo,
                transactionId = transactionId
            )
            orderList.add(orderModel)
        }
        orderViewmodel.orderDataSave(
            orderList = orderList
        )
    }
}

@Composable
fun PaymentCard(
    isRadioBankCardSelected: Boolean,
    isRadioCashSelected: Boolean,
    onRadioBankCardSelected: () -> Unit,
    onRadioCashSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = MainColor
                ), shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RadioBankCardRow(
            onRadioBankCardSelected = isRadioBankCardSelected,
            onRadioBankCardClick = {
                onRadioBankCardSelected()
            }
        )
        Spacer(Modifier.height(4.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackGroundColor.copy(alpha = 0.24f))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.subtract),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = GrayColor
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "After clicking “Pay now”, you will be redirected to Sampath Bank IPG to complete your purchase securely.",
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.W400,
                    color = GrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                )
            )
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(
            thickness = 0.5.dp,
            color = GrayColor
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(WhiteColor),
            verticalAlignment = Alignment.CenterVertically, // Align vertically to center
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // RadioButton should be aligned to the top using alignment within its modifier
            RadioButton(
                selected = isRadioCashSelected,
                onClick = {
                    onRadioCashSelected()
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MainColor,
                    unselectedColor = GrayColor
                ),
                modifier = Modifier.align(Alignment.Top) // Ensure it's aligned at the top of the Row
            )

            // Text modifier remains the same
            Text(
                text = "Cash On Delivery  (COD)",
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W500,
                    color = GrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                ),
                modifier = Modifier.weight(0.6f),
                textAlign = TextAlign.Start
            )
        }
    }

}

@Composable
fun RadioBankCardRow(
    onRadioBankCardSelected: Boolean,
    onRadioBankCardClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteColor),
        verticalAlignment = Alignment.CenterVertically, // Align vertically to center
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // RadioButton should be aligned to the top using alignment within its modifier
        RadioButton(
            selected = onRadioBankCardSelected,
            onClick = {
                onRadioBankCardClick()
            },
            colors = RadioButtonDefaults.colors(
                selectedColor = MainColor,
                unselectedColor = GrayColor
            ),
            modifier = Modifier.align(Alignment.Top) // Ensure it's aligned at the top of the Row
        )

        // Text modifier remains the same
        Text(
            text = "Sampath Bank IPG",
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.W500,
                color = GrayColor,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            ),
            modifier = Modifier.weight(0.6f),
            textAlign = TextAlign.Start
        )

        // Row for icons is fine, as it seems to align properly
        Row(
            modifier = Modifier
                .weight(0.3f)
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(R.drawable.visa),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Icon(
                painter = painterResource(R.drawable.master_card),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Icon(
                painter = painterResource(R.drawable.union_pay),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }

    }
}

@Composable
fun BillingAddressColumn(
    isRadioShippingAddressSelected: Boolean,
    isRadioDifferentAddressSelected: Boolean,
    onRadioShippingAddressSelected: () -> Unit,
    onRadioDifferentAddressSelected: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteColor),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Billing Address", style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.W700,
                color = GrayColor,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Select the address that matches your card or payment method.",
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.W400,
                color = GrayColor,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            )
        )
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(
                        width = 1.dp,
                        color = MainColor
                    ), shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RadioButtonWithTextPayment(
                radioSelected = isRadioShippingAddressSelected,
                textMessage = "Same as shipping address",
                onClick = {
                    onRadioShippingAddressSelected()
                }
            )
            HorizontalDivider(
                thickness = 0.5.dp,
                color = GrayColor
            )
            RadioButtonWithTextPayment(
                radioSelected = isRadioDifferentAddressSelected,
                textMessage = "Use a different billing address",
                onClick = {
                    onRadioDifferentAddressSelected()
                }
            )
        }
    }
}

@Preview
@Composable
fun RadioButtonWithTextPayment(
    radioSelected: Boolean = true,
    textMessage: String = "Same as shipping address",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        RadioButton(
            selected = radioSelected,
            onClick = {
                onClick()
            },
            colors = RadioButtonDefaults.colors(
                selectedColor = MainColor,
                unselectedColor = GrayColor
            )
        )
        Text(
            text = textMessage, style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.W500,
                color = GrayColor,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            ), modifier = Modifier.weight(
                0.6f
            ), textAlign = TextAlign.Start
        )
        Text(
            text = "change", style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.W400,
                color = GrayColor,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            ), modifier = Modifier.weight(
                0.1f
            ), textAlign = TextAlign.Center
        )
    }
}
