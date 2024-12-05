package com.tech.indiaekartshoppinguser.presentation.screens.product

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.domain.models.toCartModel
import com.tech.indiaekartshoppinguser.presentation.navigation.Routes
import com.tech.indiaekartshoppinguser.presentation.screens.components.ButtonComponent
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ShoppingViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.CardBackGroundColor
import com.tech.indiaekartshoppinguser.ui.theme.DarkGrayColor
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.PurchaseLogoColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun ProductDetailScreen(
    productId: String,
    shoppingViewmodel: ShoppingViewmodel,
    navController: NavController
) {
    val getProductByIdState = shoppingViewmodel.getProductByIdState.collectAsStateWithLifecycle()
    val productInFavouritePerformState =
        shoppingViewmodel.productInFavouritePerformState.collectAsStateWithLifecycle()
    val checkProductInFavouriteOrNotState =
        shoppingViewmodel.checkProductInFavouriteOrNotState.collectAsStateWithLifecycle()

    val productInCartPerformState =
        shoppingViewmodel.productInCartPerformState.collectAsStateWithLifecycle()
    val checkProductInCartOrNotState =
        shoppingViewmodel.checkProductInCartOrNotState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        shoppingViewmodel.checkProductInFavouriteOrNot(productId)
        shoppingViewmodel.checkProductInCartOrNot(productId)
        shoppingViewmodel.getProductById(productId)
    }

    val context = LocalContext.current
    var count by rememberSaveable {
        mutableIntStateOf(1)
    }
    var sizeSelected by rememberSaveable {
        mutableStateOf("")
    }
    var colorSelected by rememberSaveable {
        mutableIntStateOf(Color.Transparent.toArgb())
    }

    if (productInFavouritePerformState.value.message != null) {
        Toast.makeText(context, productInFavouritePerformState.value.message, Toast.LENGTH_SHORT)
            .show()
        shoppingViewmodel.checkProductInFavouriteOrNot(productId)
        shoppingViewmodel.resetProductInFavouriteState()
    } else if (productInFavouritePerformState.value.error.isNotEmpty()) {
        Toast.makeText(context, productInFavouritePerformState.value.error, Toast.LENGTH_SHORT)
            .show()
        shoppingViewmodel.resetProductInFavouriteState()
    }
    if (productInCartPerformState.value.message != null) {
//        Toast.makeText(context, productInCartPerformState.value.message, Toast.LENGTH_SHORT)
//            .show()
        shoppingViewmodel.checkProductInCartOrNot(productId)
        shoppingViewmodel.resetProductInCartState()
    } else if (productInCartPerformState.value.error.isNotEmpty()) {
        Toast.makeText(context, productInCartPerformState.value.error, Toast.LENGTH_SHORT).show()
        shoppingViewmodel.resetProductInCartState()
    }
    when {
        getProductByIdState.value.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GrayColor.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MainColor)
            }
        }

        getProductByIdState.value.error.isNotBlank() -> {
            Toast.makeText(context, getProductByIdState.value.error, Toast.LENGTH_SHORT).show()
        }

        getProductByIdState.value.productData != null -> {
            val productData = getProductByIdState.value.productData
            // Handle product data here
            val colorList = listOf(
                MainColor,
                BlackColor,
                GrayColor,
                DarkGrayColor,
                PurchaseLogoColor,
                CardBackGroundColor
            )
            val sizeList = listOf(
                "UK 10",
                "UK 11",
                "UK 12",
                "UK 13",
                "UK 14",
                "UK 15",
                "UK 16",
                "UK 17",
                "UK 18",
                "UK 19",
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WhiteColor)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopStart // Align content at the bottom of Box
                ) {
                    AsyncImage(
                        model = productData?.productImageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.fork_1),
                        error = painterResource(R.drawable.fork_1)
                    )

                    // Back button Icon with shadow and background for visibility
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                            .shadow(
                                elevation = 4.dp, // Add a slight shadow for better contrast
                                shape = CircleShape
                            )
                            .background(WhiteColor)
                            .size(36.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navController.navigateUp()
                            }
                    )

                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = productData?.productName!!, style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W700,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Image(
                        painter = painterResource(R.drawable.rate),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(Modifier.height(4.dp))
                    val annotatedString = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = BlackColor, fontSize = 16.sp)) {
                            append("Rs: ")
                        }
                        withStyle(style = SpanStyle(color = BlackColor, fontSize = 24.sp)) {
                            append(productData.productFinalPrice)
                        }
                    }
                    Text(
                        text = annotatedString,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W400,
                            textAlign = TextAlign.End,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Size",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W400,
                                textAlign = TextAlign.End,
                                color = BlackColor,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold))
                            )
                        )
                        Text(
                            text = "see more",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W400,
                                textAlign = TextAlign.End,
                                color = MainColor,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold))
                            )
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LazyRow(
                            modifier = Modifier.weight(0.7f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            items(sizeList) { size ->
                                SizeBox(
                                    text = size,
                                    isSelected = size == sizeSelected // Check if the box is selected
                                ) {
                                    sizeSelected = size // Update the selected size
                                }

                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Row(
                            modifier = Modifier
                                .weight(0.4f)
                                .background(PurchaseLogoColor, shape = RoundedCornerShape(8.dp)).padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.plus),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        count++
                                    }
                            )
                            Spacer(Modifier.width(12.dp))
                            SizeBox(
                                text = count.toString(), isSelected = false
                            )
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_minus),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        if (count > 1)
                                            count--
                                    }
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Color",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W400,
                            textAlign = TextAlign.End,
                            color = BlackColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            items(colorList) {
                                ColorBox(
                                    color = it,
                                    colorSelected = it.toArgb() == colorSelected
                                ) {
                                    colorSelected = it.toArgb()
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Specification",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W600,
                            textAlign = TextAlign.End,
                            color = BlackColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = productData.productDescription,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                            textAlign = TextAlign.Start,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_medium))
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    ButtonComponent(
                        text = "Buy Now",
                    ) {
                        if (colorSelected != Color.Transparent.toArgb() && sizeSelected.isNotEmpty()) {
                            navController.navigate(Routes.ShippingScreen(
                                productId = productId,
                                productQty = count.toString(),
                                color = colorSelected.toString(),
                                size = sizeSelected
                            ))
                        } else {
                            Toast.makeText(
                                context,
                                "Please select color and size",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    if (checkProductInCartOrNotState.value.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MainColor)
                        }
                    } else {
                        ButtonComponent(
                            text = if (checkProductInCartOrNotState.value.isProductInCart) "Go to Cart" else "Add to Cart",
                            containerColor = GrayColor
                        ) {
                            if (checkProductInCartOrNotState.value.isProductInCart) {
                                navController.navigate(Routes.CartScreen)
                            } else {
                                if (colorSelected != Color.Transparent.toArgb() && sizeSelected.isNotEmpty()) {
                                    val cartModel = productData.toCartModel(
                                        productQty = count.toString(),
                                        color = colorSelected.toString(),
                                        size = sizeSelected
                                    )
                                    shoppingViewmodel.productInCartPerform(cartModel)
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please select color and size",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (productInFavouritePerformState.value.isLoading) {
                            CircularProgressIndicator(
                                color = MainColor,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {

                            if (checkProductInFavouriteOrNotState.value.isProductInFavourite) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MainColor
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = MainColor
                                )
                            }

                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "Add to Wishlist",
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.W700,
                                    textAlign = TextAlign.End,
                                    color = MainColor,
                                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                                ), modifier = Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    shoppingViewmodel.productInFavouritePerform(productData)
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun SizeBox(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
            .padding(top = 8.dp, end = 8.dp, bottom = 8.dp)
            .size(40.dp)
            .background(
                if (isSelected) MainColor else WhiteColor, shape = RoundedCornerShape(
                    8.dp
                )
            )
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = MainColor
                ),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.End,
                color = if (isSelected) WhiteColor else BlackColor,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            )
        )
    }
}

@Composable
fun ColorBox(
    color: Color,
    colorSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
            .padding(top = 8.dp, end = 8.dp, bottom = 8.dp)
            .size(40.dp)
            .background(
                color, shape = RoundedCornerShape(
                    8.dp
                )
            )
            .border(
                width = 2.dp,
                color = if (colorSelected) MainColor else WhiteColor,
                shape = RoundedCornerShape(8.dp)
            )
    )
}

