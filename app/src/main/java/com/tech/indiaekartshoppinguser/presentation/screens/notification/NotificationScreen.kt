package com.tech.indiaekartshoppinguser.presentation.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.presentation.screens.notification.components.NotificationProductView
import com.tech.indiaekartshoppinguser.presentation.screens.notification.components.NotificationSuccessfulPurchaseView
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun NotificationScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteColor).padding(8.dp), verticalArrangement = Arrangement.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = BlackColor,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(36.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        navController.navigateUp()
                    }
            )
            Text(
                text = "Notifications",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.Start,
                    color = BlackColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                )
            )
        }
        Spacer(Modifier.height(16.dp))

        NotificationSuccessfulPurchaseView()
        Spacer(Modifier.height(8.dp))
        NotificationProductView()

    }
}