package com.tech.indiaekartshoppinguser.presentation.screens.cart.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.times
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.domain.models.CartModel
import com.tech.indiaekartshoppinguser.ui.theme.DarkGrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor
import kotlin.time.times

@Composable
fun ShoppingCartEachRow(cartModel: CartModel,onRemoveClick:()->Unit) {

    Row(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .padding(top = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Absolute.Left
    ) {
        Card(
            modifier = Modifier
                .height(100.dp)
                .width(70.dp)
                .background(WhiteColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            AsyncImage(
                model = cartModel.productImageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.fork_1),
                error = painterResource(R.drawable.fork_1)
            )
        }
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cartModel.productName,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W700,
                        textAlign = TextAlign.Start,
                        color = DarkGrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    ), modifier = Modifier.weight(0.5f), maxLines = 3
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "Rs : ${cartModel.productFinalPrice}",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.W700,
                        textAlign = TextAlign.Start,
                        color = DarkGrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    ), modifier = Modifier.weight(0.3f)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = cartModel.productQty,
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.W700,
                        textAlign = TextAlign.Center,
                        color = DarkGrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    ), modifier = Modifier.weight(0.3f)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = (cartModel.productFinalPrice.toInt() * cartModel.productQty.toInt()).toString(),
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.W700,
                        textAlign = TextAlign.Start,
                        color = DarkGrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_bold))
                    ), modifier = Modifier.weight(0.3f)
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = DarkGrayColor,
                    modifier = Modifier.size(12.dp).clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ){
                        onRemoveClick()
                    }
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "GF10125",
                style = TextStyle(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.Start,
                    color = DarkGrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                ),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = cartModel.size,
                style = TextStyle(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.Start,
                    color = DarkGrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                ),
            )
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Color :",
                    style = TextStyle(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.W700,
                        textAlign = TextAlign.Start,
                        color = DarkGrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    ),
                )
                Spacer(Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .background(Color(cartModel.color.toInt()))
                )
            }
        }
    }
}