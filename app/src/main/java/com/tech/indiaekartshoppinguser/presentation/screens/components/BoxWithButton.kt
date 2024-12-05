package com.tech.indiaekartshoppinguser.presentation.screens.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor

@Composable
fun BoxWithButton(
    buttonText : String,
    @DrawableRes icon : Int,
    onClick:()->Unit
) {

    Box(
        modifier = Modifier.fillMaxWidth().clickable {
            onClick()
        },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    border = BorderStroke(
                        1.dp, MainColor
                    ), shape = RoundedCornerShape(16.dp)
                ).padding(start = 16.dp,end = 16.dp,top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(icon), contentDescription = null,
                modifier = Modifier.size(35.dp), tint = Color.Unspecified
            )
            Text(
                text = buttonText,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Center,
                    color = GrayColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                ),modifier = Modifier.weight(2f)
            )
        }
    }

}