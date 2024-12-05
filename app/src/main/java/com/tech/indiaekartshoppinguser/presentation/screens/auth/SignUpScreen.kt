package com.tech.indiaekartshoppinguser.presentation.screens.auth

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.tech.indiaekartshoppinguser.presentation.navigation.SubNavigation
import com.tech.indiaekartshoppinguser.presentation.screens.components.BoxWithButton
import com.tech.indiaekartshoppinguser.presentation.screens.components.ButtonComponent
import com.tech.indiaekartshoppinguser.presentation.screens.components.MulticolorText
import com.tech.indiaekartshoppinguser.presentation.screens.components.TextFieldComponent
import com.tech.indiaekartshoppinguser.presentation.screens.profile.components.LogoutAlertDialog
import com.tech.indiaekartshoppinguser.presentation.viewmodel.AuthViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.BlackColor
import com.tech.indiaekartshoppinguser.ui.theme.GrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun SignUpScreen(navController: NavController, authViewmodel: AuthViewmodel,onSignInClick: () -> Unit) {

    val userSignUpState = authViewmodel.userRegisterState.collectAsState()
    val userRegisterScreenState = authViewmodel.userRegisterScreenState.collectAsState()
    val context = LocalContext.current

    val showAlertDialog = rememberSaveable {
        mutableStateOf(false)
    }
    if(showAlertDialog.value){
        RegisterAlertDialog(
            showDialog = showAlertDialog,
            onConfirm = {
               showAlertDialog.value =!showAlertDialog.value
                navController.navigate(Routes.HomeScreen)
            }
        )
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Signup",
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextFieldComponent(
                        value = userRegisterScreenState.value.firstName.value,
                        placeholderText = "First Name",
                        modifier = Modifier.weight(1f)
                    ) {
                        userRegisterScreenState.value.firstName.value = it
                    }
                    Spacer(Modifier.width(16.dp))
                    TextFieldComponent(
                        value = userRegisterScreenState.value.lastName.value,
                        placeholderText = "Last Name",
                        modifier = Modifier.weight(1f)
                    ) {
                        userRegisterScreenState.value.lastName.value = it
                    }
                }
                Spacer(Modifier.height(8.dp))
                TextFieldComponent(
                    value = userRegisterScreenState.value.email.value,
                    placeholderText = "Email"
                ) {
                    userRegisterScreenState.value.email.value = it
                }
                Spacer(Modifier.height(8.dp))
                TextFieldComponent(
                    value = userRegisterScreenState.value.password.value,
                    placeholderText = "Create Password"
                ) {
                    userRegisterScreenState.value.password.value = it
                }
                Spacer(Modifier.height(8.dp))
                TextFieldComponent(
                    value = userRegisterScreenState.value.confirmPassword.value,
                    placeholderText = "Confirm Password"
                ) {
                    userRegisterScreenState.value.confirmPassword.value = it
                }
                Spacer(Modifier.height(8.dp))
                ButtonComponent(
                    text = "Signup",
                ) {
                    authViewmodel.registerUserWithEmailPassword(
                        userModel = UserModel(
                            firstName = userRegisterScreenState.value.firstName.value,
                            lastName = userRegisterScreenState.value.lastName.value,
                            phoneNumber = userRegisterScreenState.value.phoneNumber.value,
                            address = userRegisterScreenState.value.address.value,
                            userImage = userRegisterScreenState.value.userImage.value,
                            email = userRegisterScreenState.value.email.value,
                            password = userRegisterScreenState.value.password.value,
                            uuid = userRegisterScreenState.value.uuid.value
                        )
                    )
                }
                Spacer(Modifier.height(8.dp))
                MulticolorText(
                    firstText = "Already have an account? ",
                    secondText = "Login"
                ) {
                    navController.navigate(Routes.LoginScreen) {
                        launchSingleTop = true
                        popUpTo(Routes.SignUpScreen) {
                            inclusive = true
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
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
                        text = "OR",
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
    when{
        userSignUpState.value.isLoading->{
            Box(
                modifier = Modifier.fillMaxSize().background(GrayColor.copy(alpha = 0.5f)), contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(color = MainColor)
            }
        }
        userSignUpState.value.error.isNotEmpty()-> {
            Toast.makeText(context, userSignUpState.value.error, Toast.LENGTH_SHORT).show()
        }
        userSignUpState.value.data?.isNotEmpty() == true -> {

            LaunchedEffect(Unit) {
                showAlertDialog.value = true
            }
            Toast.makeText(context, userSignUpState.value.data, Toast.LENGTH_SHORT).show()
            authViewmodel.clearAuthUserState()
        }
    }


}