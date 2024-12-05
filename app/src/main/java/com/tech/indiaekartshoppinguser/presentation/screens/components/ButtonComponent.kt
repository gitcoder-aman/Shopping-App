package com.tech.indiaekartshoppinguser.presentation.screens.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun ButtonComponent(
    text : String,
    containerColor : Color = MainColor,
    onClick:()->Unit
) {

    Button(
        onClick = {
            onClick()
        }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = WhiteColor
        ), shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.W600,
                textAlign = TextAlign.Start,
                color = WhiteColor,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        )
    }

}