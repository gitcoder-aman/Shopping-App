package com.tech.indiaekartshoppinguser.presentation.screens.shipping

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.domain.models.CartModel
import com.tech.indiaekartshoppinguser.domain.models.ShippingModel
import com.tech.indiaekartshoppinguser.domain.models.toCartModel
import com.tech.indiaekartshoppinguser.presentation.navigation.Routes
import com.tech.indiaekartshoppinguser.presentation.screens.components.ButtonComponent
import com.tech.indiaekartshoppinguser.presentation.screens.components.TextFieldComponent
import com.tech.indiaekartshoppinguser.presentation.screens.shipping.components.RadioButtonWithText
import com.tech.indiaekartshoppinguser.presentation.screens.shipping.stateScreen.ShippingScreenState
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ShippingViewmodel
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ShoppingViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor
import com.tech.indiaekartshoppinguser.utils.sumAllTotalPrice

@Composable
fun ShippingScreen(
    navController: NavController,
    shippingViewmodel: ShippingViewmodel,
    shoppingViewmodel: ShoppingViewmodel,
    productId: String,
    color: String,
    size: String,
    productQty: String
) {

    val shippingScreenState = shippingViewmodel.shippingScreenState.collectAsStateWithLifecycle()
    val shippingState = shippingViewmodel.shippingState.collectAsStateWithLifecycle()
    val getAllCartProducts = shoppingViewmodel.getAllCartProducts.collectAsStateWithLifecycle()
    val getProductByIdState = shoppingViewmodel.getProductByIdState.collectAsStateWithLifecycle()
    val shippingDataState = shippingViewmodel.getShippingState.collectAsStateWithLifecycle()


    val context = LocalContext.current
    LaunchedEffect(Unit) {
        shippingViewmodel.getShippingDataThroughUID()
        if (productId.isNotEmpty()) {
            shoppingViewmodel.getProductById(productId)
        }
    }

    var isRadioCashSelected by rememberSaveable {
        mutableStateOf(false)
    }
    var isRadioFreeSelected by rememberSaveable {
        mutableStateOf(false)
    }

    if (getAllCartProducts.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MainColor)
        }
    } else if (getAllCartProducts.value.error.isNotEmpty()) {
        Toast.makeText(context, getAllCartProducts.value.error, Toast.LENGTH_SHORT).show()
    } else {
        val cartProductList = getAllCartProducts.value.cartProductData
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
                text = "Shipping", style = TextStyle(
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
                    text = "Return to cart", style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600,
                        color = GrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    )
                )
            }
            Spacer(Modifier.height(4.dp))
            if (cartProductList.isNotEmpty()) {
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
            } else {
                val productData = getProductByIdState.value.productData?.toCartModel(
                    productQty = productQty,
                    color = color,
                    size = size
                )
                if (productData != null)
                    ShippingProductColumn(productData, modifier = Modifier.fillMaxWidth())
            }
            Spacer(Modifier.height(4.dp))
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
                    text = if (cartProductList.isNotEmpty()) {
                        "Rs: "+sumAllTotalPrice(cartProductList)
                    } else {
                        "Rs: "+(getProductByIdState.value.productData?.productFinalPrice?.toInt()
                            ?.times(productQty.toInt()))
                    }, style = TextStyle(
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
            ContactInfoColumn(shippingScreenState)
            Spacer(Modifier.height(4.dp))
            ShippingAddressInfoColumn(
                checked = shippingScreenState.value.isSaveForNextTime.value,
                checkBoxSaveInfo = {
                    shippingScreenState.value.isSaveForNextTime.value =
                        !shippingScreenState.value.isSaveForNextTime.value
                },
                shippingScreenState
            )
            Spacer(Modifier.height(16.dp))
            ShippingMethodColumn(
                radioFreeSelected = isRadioFreeSelected,
                radioCashSelected = isRadioCashSelected,
                radioCashButtonOnClick = {
                    isRadioCashSelected = !isRadioCashSelected
                    if (isRadioCashSelected)
                        isRadioFreeSelected = false
                },
                radioFreeButtonOnClick = {
                    isRadioFreeSelected = !isRadioFreeSelected
                    if (isRadioFreeSelected)
                        isRadioCashSelected = false
                }
            )
            Spacer(Modifier.height(16.dp))
            ButtonComponent(
                text = "Continue to Shipping",
                containerColor = GrayColor,
            ) {
                val shoppingModel = ShippingModel(
                    email = shippingScreenState.value.email.value,
                    mobileNo = shippingScreenState.value.mobileNo.value,
                    countryRegion = shippingScreenState.value.countryRegion.value,
                    firstName = shippingScreenState.value.firstName.value,
                    lastName = shippingScreenState.value.lastName.value,
                    address = shippingScreenState.value.address.value,
                    city = shippingScreenState.value.city.value,
                    postalCode = shippingScreenState.value.postalCode.value,
                    saveForNextTime = shippingScreenState.value.isSaveForNextTime.value
                )
                val error = validateShippingModel(shippingModel = shoppingModel)

                if (error.isEmpty()) {
                    shippingViewmodel.shippingDataSave(shoppingModel)
                    navController.navigate(Routes.PaymentScreen)
                } else {
                    error.forEach {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    when {
        shippingState.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MainColor)
            }
        }

        shippingState.value.data.isNotEmpty() -> {
            Log.d("@shipping", "ShippingScreen: ${shippingState.value.data}")
        }

        shippingState.value.error.isNotEmpty() -> {
            Log.d("@shipping", "ShippingScreen: ${shippingState.value.error}")
            Toast.makeText(context, shippingState.value.error, Toast.LENGTH_SHORT).show()
        }
    }
    if (getProductByIdState.value.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MainColor)
        }
    }
    when {
        shippingDataState.value.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MainColor)
            }
        }

        shippingDataState.value.data != null -> {
            Log.d("@payment", "ShippingScreen: ${shippingDataState.value.data?.saveForNextTime}")
            if (shippingDataState.value.data!!.saveForNextTime) {
                shippingScreenState.value.email.value =
                    shippingDataState.value.data?.email.toString()
                shippingScreenState.value.mobileNo.value =
                    shippingDataState.value.data?.mobileNo.toString()
                shippingScreenState.value.countryRegion.value =
                    shippingDataState.value.data?.countryRegion.toString()
                shippingScreenState.value.firstName.value =
                    shippingDataState.value.data?.firstName.toString()
                shippingScreenState.value.lastName.value =
                    shippingDataState.value.data?.lastName.toString()
                shippingScreenState.value.address.value =
                    shippingDataState.value.data?.address.toString()
                shippingScreenState.value.city.value = shippingDataState.value.data?.city.toString()
                shippingScreenState.value.postalCode.value =
                    shippingDataState.value.data?.postalCode.toString()
                shippingScreenState.value.isSaveForNextTime.value =
                    shippingDataState.value.data?.saveForNextTime!!
            }
        }

        shippingDataState.value.error.isNotEmpty() -> {
            Toast.makeText(context, shippingDataState.value.error, Toast.LENGTH_SHORT).show()
        }
    }
}

fun validateShippingModel(shippingModel: ShippingModel): List<String> {
    val errors = mutableListOf<String>()

    if (shippingModel.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(shippingModel.email)
            .matches()
    ) {
        errors.add("Invalid email address.")
    }

    if (shippingModel.mobileNo.isBlank() || shippingModel.mobileNo.length != 10 || !shippingModel.mobileNo.all { it.isDigit() }) {
        errors.add("Invalid mobile number. It must be 10 digits.")
    }

    if (shippingModel.countryRegion.isBlank()) {
        errors.add("Country/Region cannot be empty.")
    }

    if (shippingModel.firstName.isBlank()) {
        errors.add("First name cannot be empty.")
    }

    if (shippingModel.lastName.isBlank()) {
        errors.add("Last name cannot be empty.")
    }

    if (shippingModel.address.isBlank()) {
        errors.add("Address cannot be empty.")
    }

    if (shippingModel.city.isBlank()) {
        errors.add("City cannot be empty.")
    }

    if (shippingModel.postalCode.isBlank() || shippingModel.postalCode.length < 4 || shippingModel.postalCode.length > 10) {
        errors.add("Invalid postal code. It must be between 4 and 10 characters.")
    }

    return errors
}

@Composable
fun ShippingProductColumn(
    cartProduct: CartModel,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .height(155.dp)
            .background(WhiteColor)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Card(
                modifier = Modifier
                    .height(77.dp)
                    .width(55.dp)
                    .background(WhiteColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                AsyncImage(
                    model = cartProduct.productImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.fork_1),
                    error = painterResource(R.drawable.fork_1)
                )
            }
            Spacer(Modifier.width(4.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = cartProduct.productName, style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.W700,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    )
                    Text(
                        text = cartProduct.productFinalPrice, style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.W700,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "GF1025", style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.W700,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        ), modifier = Modifier.weight(0.8f)
                    )
                    Text(
                        text = "x" + cartProduct.productQty, style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.W700,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                            textAlign = TextAlign.End
                        )
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = cartProduct.size, style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.W700,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular))
                        )
                    )
                    Spacer(Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .shadow(
                                elevation = 1.dp,
                                shape = CircleShape
                            )
                            .background(MainColor)
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(
            thickness = 0.5.dp,
            color = GrayColor
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Sub Total", style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W700,
                    color = GrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
            )
            Text(
                text = cartProduct.productFinalPrice, style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W700,
                    color = GrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Shipping", style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W600,
                    color = GrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
            )
            Text(
                text = "Free", style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W600,
                    color = GrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
            )
        }
    }
}

@Composable
fun ContactInfoColumn(shippingScreenState: State<ShippingScreenState>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteColor),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Contact Information", style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.W700,
                color = GrayColor,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
        )
        Spacer(Modifier.height(8.dp))
        TextFieldComponent(
            value = shippingScreenState.value.email.value,
            onValueChange = {
                shippingScreenState.value.email.value = it
            },
            placeholderText = "Email"
        )
        Spacer(Modifier.height(4.dp))
        TextFieldComponent(
            value = shippingScreenState.value.mobileNo.value,
            onValueChange = {
                shippingScreenState.value.mobileNo.value = it
            },
            placeholderText = "Mobile Number"
        )
    }
}

@Composable
fun ShippingAddressInfoColumn(
    checked: Boolean,
    checkBoxSaveInfo: () -> Unit,
    shippingScreenState: State<ShippingScreenState>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteColor),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Shipping Address", style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.W700,
                color = GrayColor,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            )
        )
        Spacer(Modifier.height(4.dp))
        TextFieldComponent(
            value = shippingScreenState.value.countryRegion.value,
            onValueChange = {
                shippingScreenState.value.countryRegion.value = it
            },
            placeholderText = "Country/Region"
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextFieldComponent(
                value = shippingScreenState.value.firstName.value,
                onValueChange = {
                    shippingScreenState.value.firstName.value = it
                },
                placeholderText = "First Name",
                modifier = Modifier.weight(0.5f)
            )
            TextFieldComponent(
                value = shippingScreenState.value.lastName.value,
                onValueChange = {
                    shippingScreenState.value.lastName.value = it
                },
                placeholderText = "Last Name",
                modifier = Modifier.weight(0.5f)
            )
        }
        Spacer(Modifier.height(4.dp))
        TextFieldComponent(
            value = shippingScreenState.value.address.value,
            onValueChange = {
                shippingScreenState.value.address.value = it
            },
            placeholderText = "Address"
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextFieldComponent(
                value = shippingScreenState.value.city.value,
                onValueChange = {
                    shippingScreenState.value.city.value = it
                },
                placeholderText = "City",
                modifier = Modifier.weight(0.5f)
            )
            TextFieldComponent(
                value = shippingScreenState.value.postalCode.value,
                onValueChange = {
                    shippingScreenState.value.postalCode.value = it
                },
                placeholderText = "Postal Code",
                modifier = Modifier.weight(0.5f)
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checkBoxSaveInfo()
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MainColor,
                    uncheckedColor = GrayColor,
                    disabledUncheckedColor = GrayColor
                ),
                modifier = Modifier
                    .shadow(
                        elevation = 0.dp,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(WhiteColor)
                    .size(12.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Save this information for next time", style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W400,
                    color = GrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_medium))
                )
            )
        }
    }
}

@Composable
fun ShippingMethodColumn(
    radioFreeSelected: Boolean,
    radioCashSelected: Boolean,
    radioFreeButtonOnClick: () -> Unit,
    radioCashButtonOnClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteColor),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Shipping Method", style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.W700,
                color = GrayColor,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
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
            RadioButtonWithText(
                radioSelected = radioFreeSelected,
                textMessage = "Standard FREE delivery over Rs:4500",
                charge = "Free",
                onClick = {
                    radioFreeButtonOnClick()
                }
            )
            HorizontalDivider(
                thickness = 0.5.dp,
                color = GrayColor
            )
            RadioButtonWithText(
                radioSelected = radioCashSelected,
                textMessage = "Cash on delivery over Rs:4500 (Free Delivery, COD processing fee only)",
                charge = "100",
                onClick = {
                    radioCashButtonOnClick()
                }
            )
        }
    }
}

