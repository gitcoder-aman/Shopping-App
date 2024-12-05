package com.tech.indiaekartshoppinguser.presentation.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor

@Composable
fun MulticolorText(
    firstText: String, secondText: String,
    onClick: () -> Unit
) {

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Black)) {
            append(firstText)
        }
        withStyle(style = SpanStyle(color = MainColor)) {
            append(secondText)
        }
    }
    Text(
        text = annotatedString,
        style = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.End,
            color = GrayColor,
            fontFamily = FontFamily(Font(R.font.montserrat_regular))
        ), modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            onClick()
        })
}