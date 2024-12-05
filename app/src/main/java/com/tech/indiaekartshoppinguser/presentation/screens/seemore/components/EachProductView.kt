package com.tech.indiaekartshoppinguser.presentation.screens.seemore.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.tech.indiaekartshoppinguser.domain.models.ProductModel
import com.tech.indiaekartshoppinguser.ui.theme.DarkGrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun EachProductView(
    productModel: ProductModel, onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteColor)
            .padding(8.dp)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null
            ) {
                onClick()
            },
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
                model = productModel.productImageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.fork_1),
                error = painterResource(R.drawable.fork_1)
            )
        }
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = productModel.productName,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W700,
                        textAlign = TextAlign.Start,
                        color = DarkGrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    ),
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = "Rs : ${productModel.productFinalPrice}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W700,
                        textAlign = TextAlign.Start,
                        color = DarkGrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    ),
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "GF10125",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.Start,
                    color = DarkGrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                ),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Size : UK10",
                style = TextStyle(
                    fontSize = 12.sp,
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
                        fontSize = 12.sp,
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
                        .background(MainColor)
                )
            }
        }
    }

}