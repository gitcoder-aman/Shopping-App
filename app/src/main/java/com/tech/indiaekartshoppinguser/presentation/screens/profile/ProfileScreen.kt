package com.tech.indiaekartshoppinguser.presentation.screens.profile

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.tech.indiaekartshoppinguser.R
import com.tech.indiaekartshoppinguser.domain.models.UserModel
import com.tech.indiaekartshoppinguser.presentation.navigation.Routes
import com.tech.indiaekartshoppinguser.presentation.screens.components.ButtonComponent
import com.tech.indiaekartshoppinguser.presentation.screens.components.TextFieldComponent
import com.tech.indiaekartshoppinguser.presentation.screens.profile.components.BorderBoxButton
import com.tech.indiaekartshoppinguser.presentation.screens.profile.components.LogoutAlertDialog
import com.tech.indiaekartshoppinguser.presentation.viewmodel.ProfileViewmodel
import com.tech.indiaekartshoppinguser.ui.theme.DarkGrayColor
import com.tech.indiaekartshoppinguser.ui.theme.MainColor
import com.tech.indiaekartshoppinguser.ui.theme.WhiteColor

@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewmodel: ProfileViewmodel,
    firebaseAuth: FirebaseAuth
) {

    val getProfileDataState = profileViewmodel.getProfileDataState.collectAsStateWithLifecycle()
    val profileScreenDataState = profileViewmodel.getProfileScreenDataState.collectAsStateWithLifecycle()
    val updateProfileDataState = profileViewmodel.updateProfileDataState.collectAsStateWithLifecycle()
    val uploadUserImageState = profileViewmodel.uploadUserImageState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        profileViewmodel.getProfileData(firebaseAuth.currentUser?.uid!!)
    }
    val context = LocalContext.current
    var isEditable by rememberSaveable {
        mutableStateOf(true)
    }
    val showAlertDialog = rememberSaveable {
        mutableStateOf(false)
    }
    if (showAlertDialog.value) {
        LogoutAlertDialog(
            showDialog = showAlertDialog,
            userImage = profileScreenDataState.value.userImageUrl.value,
            onConfirm = {
                firebaseAuth.signOut()
                navController.navigate(Routes.LoginScreen)
                showAlertDialog.value = false
            },
            onCancel = {
                showAlertDialog.value = false
            }
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) {
                profileScreenDataState.value.userImageUri.value = it
            }
        }
    )

    when {
        getProfileDataState.value.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MainColor)
            }
        }

        getProfileDataState.value.error.isNotBlank() -> {
            Toast.makeText(context, getProfileDataState.value.error, Toast.LENGTH_SHORT).show()
        }

        getProfileDataState.value.data != null -> {
            LaunchedEffect(getProfileDataState.value.data) {

                profileScreenDataState.value.firstName.value =
                    getProfileDataState.value.data!!.firstName
                profileScreenDataState.value.lastName.value =
                    getProfileDataState.value.data!!.lastName
                profileScreenDataState.value.email.value = getProfileDataState.value.data!!.email
                profileScreenDataState.value.phoneNumber.value =
                    getProfileDataState.value.data!!.phoneNumber
                profileScreenDataState.value.address.value =
                    getProfileDataState.value.data!!.address
                profileScreenDataState.value.userImageUrl.value =
                    getProfileDataState.value.data!!.userImage
                profileScreenDataState.value.password.value =
                    getProfileDataState.value.data!!.password
                Log.d("@profile", "ProfileScreen: ${getProfileDataState.value.data!!.uuid}")
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
                        .padding(top = 90.dp, start = 16.dp, end = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier.size(90.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = profileScreenDataState.value.userImageUrl.value.ifEmpty {
                                if (profileScreenDataState.value.userImageUri.value != null) {
                                    profileScreenDataState.value.userImageUri.value
                                } else {
                                    R.drawable.profile_image
                                }
                            },
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(90.dp)
                                .shadow(
                                    elevation = 1.dp,
                                    shape = CircleShape
                                ).clickable(
                                    interactionSource = remember {
                                        MutableInteractionSource()
                                    },
                                    indication = null
                                ) {
                                    if (!isEditable) {
                                        launcher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                },
                            loading = {
                                CircularProgressIndicator(
                                    color = MainColor,
                                )
                            }
                        )
                        if(!isEditable) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp).align(
                                    Alignment.BottomEnd
                                ).shadow(
                                    elevation = 1.dp,
                                    shape = CircleShape
                                ).background(MainColor), tint = WhiteColor
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextFieldComponent(
                            value = profileScreenDataState.value.firstName.value,
                            placeholderText = "First Name",
                            modifier = Modifier.weight(1f),
                            readOnly = isEditable
                        ) {
                            profileScreenDataState.value.firstName.value = it
                        }
                        Spacer(Modifier.width(16.dp))
                        TextFieldComponent(
                            value = profileScreenDataState.value.lastName.value,
                            placeholderText = "Last Name",
                            modifier = Modifier.weight(1f),
                            readOnly = isEditable
                        ) {
                            profileScreenDataState.value.lastName.value = it
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    TextFieldComponent(
                        value = profileScreenDataState.value.email.value,
                        placeholderText = "Email",
                        readOnly = true,
                        keyboardType = KeyboardType.Email
                    ) {
                        //No Change of email
//                profileScreenDataState.value.email.value = it
                    }
                    Spacer(Modifier.width(16.dp))
                    TextFieldComponent(
                        value = profileScreenDataState.value.phoneNumber.value,
                        placeholderText = "Phone Number",
                        readOnly = isEditable,
                        keyboardType = KeyboardType.Number
                    ) {
                        profileScreenDataState.value.phoneNumber.value = it
                    }
                    Spacer(Modifier.width(16.dp))
                    TextFieldComponent(
                        value = profileScreenDataState.value.address.value,
                        placeholderText = "Address",
                        readOnly = isEditable
                    ) {
                        profileScreenDataState.value.address.value = it
                    }
                    Spacer(Modifier.height(16.dp))
                    ButtonComponent(
                        text = "Log out",
                    ) {
                        showAlertDialog.value = !showAlertDialog.value
                    }

                    Spacer(Modifier.height(16.dp))
                    BorderBoxButton(
                        text = if (!isEditable) "Save Profile" else "Edit Profile"
                    ) {
                        isEditable = !isEditable
                        if (isEditable) {
                            if (profileScreenDataState.value.userImageUri.value != null) {
                                profileViewmodel.uploadUserImage(
                                    imageUri = profileScreenDataState.value.userImageUri.value!!
                                )
                            } else {
                                val userModel = UserModel(
                                    firstName = profileScreenDataState.value.firstName.value,
                                    lastName = profileScreenDataState.value.lastName.value,
                                    email = profileScreenDataState.value.email.value,
                                    phoneNumber = profileScreenDataState.value.phoneNumber.value,
                                    address = profileScreenDataState.value.address.value,
                                    uuid = firebaseAuth.currentUser?.uid!!,
                                    userImage = profileScreenDataState.value.userImageUrl.value,
                                    password = profileScreenDataState.value.password.value
                                )
                                profileViewmodel.updateProfileData(userModel)
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))

                    ButtonComponent(
                        text = "Your Orders",
                    ) {
                        navController.navigate(Routes.OrderScreen)
                    }
                }
            }
        }

    }
    when {
        updateProfileDataState.value.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize().background(DarkGrayColor.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MainColor)
            }
        }

        updateProfileDataState.value.error.isNotBlank() -> {
            Toast.makeText(context, updateProfileDataState.value.error, Toast.LENGTH_SHORT).show()
        }

        updateProfileDataState.value.data != null -> {

            profileViewmodel.clearUpdateProfileDataState()
            Toast.makeText(context, updateProfileDataState.value.data, Toast.LENGTH_SHORT).show()
        }
    }
    when {
        uploadUserImageState.value.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize().background(DarkGrayColor.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MainColor)
            }
        }

        uploadUserImageState.value.error.isNotBlank() -> {
            Toast.makeText(context, uploadUserImageState.value.error, Toast.LENGTH_SHORT).show()
        }

        uploadUserImageState.value.data != null -> {
            LaunchedEffect(Unit) {
                profileScreenDataState.value.userImageUrl.value = uploadUserImageState.value.data!!
                val userModel = UserModel(
                    firstName = profileScreenDataState.value.firstName.value,
                    lastName = profileScreenDataState.value.lastName.value,
                    email = profileScreenDataState.value.email.value,
                    phoneNumber = profileScreenDataState.value.phoneNumber.value,
                    address = profileScreenDataState.value.address.value,
                    uuid = firebaseAuth.currentUser?.uid!!,
                    userImage = profileScreenDataState.value.userImageUrl.value,
                    password = profileScreenDataState.value.password.value
                )
                profileViewmodel.updateProfileData(userModel)
            }
        }
    }
}