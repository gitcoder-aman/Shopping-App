package com.tech.indiaekartshoppinguser.presentation.screens.shipping.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor

@Composable
fun RadioButtonWithText(
    radioSelected: Boolean,
    textMessage: String,
    charge: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        RadioButton(
            selected = radioSelected,
            onClick = {
                onClick()
            },
            colors = RadioButtonDefaults.colors(
                selectedColor = MainColor,
                unselectedColor = GrayColor
            ), modifier = Modifier.weight(0.1f)
        )
        Text(
            text = textMessage, style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.W500,
                color = GrayColor,
                fontFamily = FontFamily(Font(R.font.montserrat_medium))
            ), modifier = Modifier.weight(
                0.8f
            ), textAlign = TextAlign.Start
        )
        Text(
            text = charge, style = TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.W700,
                color = GrayColor,
                fontFamily = FontFamily(Font(R.font.montserrat_bold))
            ), modifier = Modifier.weight(
                0.1f
            ), textAlign = TextAlign.End
        )
    }
}