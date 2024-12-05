package com.tech.indiaekartshoppinguser.presentation.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun TextFieldComponent(
    value : String,
    placeholderText : String,
    modifier: Modifier = Modifier,
    readOnly : Boolean = false,
    singleLine : Boolean = true,
    keyboardType : KeyboardType = KeyboardType.Text,
    unfocusedContainerColor : Color = WhiteColor,
    focusedContainerColor : Color = WhiteColor,
    onValueChange:(String)->Unit
) {

    TextField(
        value = value,
        readOnly = readOnly,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,  // Numeric input
            imeAction = ImeAction.Done
        ),
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(
                text = placeholderText,
                style = TextStyle(
                    fontSize = 13.sp,
                    color = GrayColor,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular))
                )
            )
        }, maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = BlackColor,
            unfocusedTextColor = Color.Unspecified,
            cursorColor = MainColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = focusedContainerColor,
            unfocusedContainerColor = unfocusedContainerColor,
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
            focusedPlaceholderColor = MainColor,
            unfocusedPlaceholderColor = MainColor

        ), modifier = modifier.padding(4.dp).fillMaxWidth().border(
            BorderStroke(
                1.dp, MainColor
            ), shape = RoundedCornerShape(16.dp)
        )
    )

}