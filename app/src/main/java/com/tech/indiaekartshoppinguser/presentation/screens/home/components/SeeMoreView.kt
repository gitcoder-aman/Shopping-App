package com.tech.indiaekartshoppinguser.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun SeeMore(
    text1: String,
    text2: String,
    onClick:()->Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(WhiteColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text1,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Start,
                color = BlackColor,
            ),
            fontFamily = FontFamily(Font(R.font.montserrat_bold))
        )
        Text(
            text = text2,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Start,
                color = MainColor,
            ),
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            }
        )

    }
}