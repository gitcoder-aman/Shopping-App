package com.tech.indiaekartshoppinguser.presentation.screens.notification.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.PurchaseLogoColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Preview
@Composable
fun NotificationSuccessfulPurchaseView() {

    Card(
        modifier = Modifier
            .fillMaxWidth().padding(8.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = WhiteColor
        ),
        elevation = CardDefaults.cardElevation(
            4.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(start = 16.dp,end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            IconBankLogo()
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Successful purchase!",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.Start,
                        color = BlackColor,
                    ),
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier  = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    Image(
                        painter = painterResource(R.drawable.time),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Just now",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                            textAlign = TextAlign.Start,
                            color = GrayColor,
                        ),
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                }
            }
        }
    }
}

@Composable
fun IconBankLogo() {
    Box(
       modifier = Modifier.size(45.dp).shadow(
           elevation = 1.dp,
           shape = RoundedCornerShape(8.dp)
       ).background(PurchaseLogoColor),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(R.drawable.subtract),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}