package com.tech.indiaekartshoppinguser.presentation.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.domain.models.ProductModel
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.DarkGrayColor
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor
import com.tech.indiaekartshoppinguser.utils.calculatePercentage

@Composable
fun ProductEachRow(productModel: ProductModel, onClick:()->Unit) {

    Column(
        modifier = Modifier.clickable(
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null
        ) {
            onClick()
        }
            .padding(start = 8.dp, end = 8.dp)
            .height(380.dp)
            .width(150.dp)
            .background(WhiteColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(200.dp), shape = RoundedCornerShape(16.dp)
        ) {
            AsyncImage(
                model = productModel.productImageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.fork_1),
                error = painterResource(R.drawable.fork_1)
            )

        }
        Spacer(
            Modifier.height(16.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteColor), border = BorderStroke(
                1.dp, GrayColor
            ), shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WhiteColor)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = productModel.productName, maxLines = 2,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = GrayColor,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    ), modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "GF1025",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = DarkGrayColor,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.Start,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                )
                Spacer(Modifier.height(8.dp))

                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MainColor, fontSize = 16.sp)) {
                        append("Rs: ")
                    }
                    withStyle(style = SpanStyle(color = MainColor, fontSize = 24.sp)) {
                        append(productModel.productFinalPrice)
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
                    val priceDiscount = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = BlackColor, fontSize = 16.sp)) {
                            append("Rs: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = BlackColor,
                                fontSize = 20.sp,
                                textDecoration = TextDecoration.LineThrough
                            )
                        ) {
                            append(productModel.productPrice)
                        }
                    }
                    Text(
                        text = priceDiscount,
                        style = TextStyle(
                            fontWeight = FontWeight.W400,
                            textAlign = TextAlign.End,
                            color = DarkGrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular))
                        )
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = calculatePercentage(
                            productModel.productPrice.toDouble(),
                            productModel.productFinalPrice.toDouble()
                        ).toString() + "% off",
                        modifier = Modifier.padding(
                            start = 4.dp,
                        ),
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                            textAlign = TextAlign.End,
                            color = MainColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    )
                }

            }
        }
    }


}