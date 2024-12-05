package com.tech.indiaekartshoppinguser.presentation.screens.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.domain.models.UserModel
import com.tech.indiaekartshoppinguser.presentation.navigation.Routes
import com.tech.indiaekartshoppinguser.presentation.screens.components.BoxWithButton
import com.tech.indiaekartshoppinguser.presentation.screens.components.ButtonComponent
import com.tech.indiaekartshoppinguser.presentation.screens.components.MulticolorText
import com.tech.indiaekartshoppinguser.presentation.screens.components.TextFieldComponent
import com.tech.indiaekartshoppinguser.presentation.viewmodel.AuthViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun LoginScreen(navController: NavController, authViewmodel: AuthViewmodel,onSignInClick: () -> Unit) {

    val userLoginState = authViewmodel.userLoginState.collectAsState()
    val userLoginScreenState = authViewmodel.userLoginScreenState.collectAsState()
    val context = LocalContext.current

    if (userLoginState.value.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MainColor)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteColor)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ellipse_1),
            contentDescription = null,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ellipse_2),
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomStart)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Login",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.W600,
                    textAlign = TextAlign.Start,
                    color = BlackColor,
                    fontFamily = FontFamily(Font(R.font.montserrat_bold))
                ), modifier = Modifier.padding(top = 124.dp, start = 36.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.End
            ) {

                TextFieldComponent(
                    value = userLoginScreenState.value.email.value,
                    placeholderText = "Email"
                ) {
                    userLoginScreenState.value.email.value = it
                }
                Spacer(Modifier.height(8.dp))
                TextFieldComponent(
                    value = userLoginScreenState.value.password.value,
                    placeholderText = "Password"
                ) {
                    userLoginScreenState.value.password.value = it
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Forgot Password?",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W400,
                        textAlign = TextAlign.End,
                        color = GrayColor,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular))
                    )
                )
                Spacer(Modifier.height(8.dp))
                ButtonComponent(
                    text = "Login",
                ) {
                    authViewmodel.loginUserWithEmailPassword(
                        userModel = UserModel(
                            email = userLoginScreenState.value.email.value,
                            password = userLoginScreenState.value.password.value
                        )
                    )
                }
                Spacer(Modifier.height(8.dp))
                MulticolorText(
                    firstText = "Don't have an account? ",
                    secondText = "Sign Up"
                ) {
                    navController.navigate(Routes.SignUpScreen) {
                        launchSingleTop = true
                        popUpTo(Routes.LoginScreen) {
                            inclusive = true
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = BlackColor,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Or",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W600,
                            textAlign = TextAlign.Center,
                            color = GrayColor,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular))
                        ), modifier = Modifier.width(50.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    HorizontalDivider(
                        thickness = 2.dp,
                        color = BlackColor,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(8.dp))
                BoxWithButton(
                    buttonText = "Login in with Facebook",
                    icon = R.drawable.facebook
                ) {

                }
                Spacer(Modifier.height(16.dp))
                BoxWithButton(
                    buttonText = "Login in with Google",
                    icon = R.drawable.google
                ) {
                    onSignInClick()
                }
            }
        }


    }
    when {
        userLoginState.value.isLoading -> {
            Log.d("@login", "LoginScreen: isLoading")
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GrayColor.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MainColor)
            }
        }

        userLoginState.value.error.isNotEmpty() -> {
            Toast.makeText(context, userLoginState.value.error, Toast.LENGTH_SHORT).show()
        }

        userLoginState.value.data?.isNotEmpty() == true -> {
            navController.navigate(Routes.HomeScreen) {
                launchSingleTop = true
                popUpTo(Routes.LoginScreen) {
                    inclusive = true
                }
            }
            authViewmodel.clearAuthUserState()
        }
    }

}

