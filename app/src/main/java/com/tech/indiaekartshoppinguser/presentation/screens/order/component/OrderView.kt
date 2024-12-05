package com.tech.indiaekartshoppinguser.presentation.screens.order.component

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.tech.indiaekartshoppinguser.domain.models.OrderModel
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun OrderView(
    orderModel: OrderModel
) {
    Column(
        modifier = Modifier
            .height(130.dp)
            .background(WhiteColor, shape = RoundedCornerShape(16.dp))
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
                    model = orderModel.productImageUrl,
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
                        text = orderModel.productName, style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.W700,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    )
                    Text(
                        text = orderModel.productFinalPrice, style = TextStyle(
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
                        text = "x" + orderModel.productQty, style = TextStyle(
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
                        text = orderModel.size, style = TextStyle(
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
                text = (orderModel.productFinalPrice.toInt() * orderModel.productQty.toInt()).toString(),
                style = TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.W700,
                    color = GrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
            )
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(
            thickness = 3.dp,
            color = GrayColor
        )
    }
}