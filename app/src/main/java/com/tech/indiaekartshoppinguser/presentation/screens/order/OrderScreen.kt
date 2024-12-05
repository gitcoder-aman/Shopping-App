package com.tech.indiaekartshoppinguser.presentation.screens.order

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.presentation.screens.order.component.OrderView
import com.tech.indiaekartshoppinguser.presentation.viewmodel.OrderViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun OrderScreen(navController: NavController, orderViewmodel: OrderViewmodel) {

    LaunchedEffect(Unit) {
        orderViewmodel.getAllOrderState()
    }
    val getAllOrderState = orderViewmodel.getAllOrderDataState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    when{
        getAllOrderState.value.isLoading->{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MainColor)
            }
        }
        getAllOrderState.value.data.isNotEmpty()->{
            val orderList = getAllOrderState.value.data
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WhiteColor)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Order", style = TextStyle(
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
                        text = "Return to Profile", style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_bold))
                        )
                    )
                }
                Spacer(Modifier.height(4.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(orderList) {
                        OrderView(orderModel = it)
                    }
                }
            }
            Log.d("@order", "OrderScreen: ${getAllOrderState.value.data.get(0).orderId}")
        }
        getAllOrderState.value.error.isNotEmpty()->{
            Toast.makeText(context, getAllOrderState.value.error, Toast.LENGTH_SHORT).show()
        }
    }

}