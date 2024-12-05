package com.tech.indiaekartshoppinguser.presentation.screens.payment

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.presentation.navigation.Routes
import com.tech.indiaekartshoppinguser.presentation.screens.components.ButtonComponent
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun SuccessFulPurchaseScreen(navController: NavHostController) {
    BackHandler {
        navController.navigate(Routes.HomeScreen){
            popUpTo(Routes.HomeScreen){
                inclusive = true
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().background(WhiteColor).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.frame_29),
            contentDescription = null,
            modifier =Modifier.size(150.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Successful purchase!",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = GrayColor,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            ),
            textAlign = TextAlign.Start
        )
        Spacer(Modifier.height(8.dp))
        ButtonComponent(
            text = "Continue Shopping",
        ) {
            navController.navigate(Routes.HomeScreen){
                popUpTo(Routes.HomeScreen){
                    inclusive = true
                }
            }
        }
    }
}