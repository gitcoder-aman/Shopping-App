package com.tech.indiaekartshoppinguser.presentation.screens.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor


@Composable
fun LogoutAlertDialog(
    userImage: String,
    showDialog: MutableState<Boolean>, // To control dialog visibility
    onConfirm: () -> Unit, // Action on confirming
    onCancel: () -> Unit// Action on canceling
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false }, // Close dialog on background click

            text = {
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center

                ) {
                    SubcomposeAsyncImage(
                        model = userImage.ifEmpty { R.drawable.profile_image },
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .shadow(
                                elevation = 1.dp,
                                shape = CircleShape
                            )
                            .size(90.dp),
                        loading = {
                            CircularProgressIndicator(
                                color = MainColor,
                            )
                        }
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "LOG OUT",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                            color = MainColor,
                            fontWeight = FontWeight.W500
                        )
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Do you Really\n" +
                                "Want To Logout?",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                            color = BlackColor,
                            fontWeight = FontWeight.W400,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ButtonBox(
                            containerColor = WhiteColor,
                            text = "Cancel",
                            textColor = MainColor
                        ) {
                            onCancel()
                        }
                        Spacer(Modifier.width(8.dp))
                        ButtonBox(
                            containerColor = MainColor,
                            text = "Log Out",
                            textColor = WhiteColor
                        ) {
                            onConfirm()
                        }
                    }
                }
            },
            dismissButton = {

            },
            confirmButton = {

            },
            shape = MaterialTheme.shapes.medium, // Rounded corners
            containerColor = MaterialTheme.colorScheme.surface, // Background color
            modifier = Modifier

        )
    }
}

@Composable
fun ButtonBox(
    containerColor: Color,
    text: String,
    textColor: Color,
    onClick: () -> Unit
) {

    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(8.dp), border = BorderStroke(
            width = 1.dp,
            color = MainColor
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                color = textColor,
                fontWeight = FontWeight.W500
            )
        )
    }
}