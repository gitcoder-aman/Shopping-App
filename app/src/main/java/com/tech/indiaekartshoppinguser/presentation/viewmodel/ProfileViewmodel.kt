package com.tech.indiaekartshoppinguser.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.indiaekartshoppinguser.common.ResultState
import com.tech.indiaekartshoppinguser.domain.models.UserModel
import com.tech.indiaekartshoppinguser.domain.usecase.GetProfileDataUseCase
import com.tech.indiaekartshoppinguser.domain.usecase.UpdateProfileDataUseCase
import com.tech.indiaekartshoppinguser.domain.usecase.UploadUserImageUseCase
import com.tech.indiaekartshoppinguser.presentation.screens.profile.stateScreen.ProfileUserDataScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewmodel @Inject constructor(
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val updateProfileDataUseCase: UpdateProfileDataUseCase,
    private val uploadUserImageUseCase: UploadUserImageUseCase
) : ViewModel() {

    private val _getProfileDataState = MutableStateFlow(GetProfileDataState())
    val getProfileDataState = _getProfileDataState.asStateFlow()

    private val _getProfileScreenDataState = MutableStateFlow(ProfileUserDataScreenState())
    val getProfileScreenDataState = _getProfileScreenDataState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUserDataScreenState()
    )

    private val _updateProfileDataState = MutableStateFlow(SetProfileDataState())
    val updateProfileDataState = _updateProfileDataState.asStateFlow()

    private val _uploadUserImageState = MutableStateFlow(UploadUserImageState())
    val uploadUserImageState = _uploadUserImageState.asStateFlow()

    fun getProfileData(uuid: String) {
        viewModelScope.launch {
            getProfileDataUseCase.invoke(uuid).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getProfileDataState.value = GetProfileDataState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getProfileDataState.value = GetProfileDataState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _getProfileDataState.value = GetProfileDataState(error = it.error)
                    }
                }
            }
        }
    }

    fun updateProfileData(userModel: UserModel) {
        viewModelScope.launch {
            updateProfileDataUseCase.invoke(userModel).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _updateProfileDataState.value = SetProfileDataState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _updateProfileDataState.value = SetProfileDataState(data = it.data)
                    }

                    is ResultState.Error -> {
                        _updateProfileDataState.value = SetProfileDataState(error = it.error)
                    }
                }
            }
        }
    }

    fun uploadUserImage(imageUri: Uri) {
        viewModelScope.launch {
            uploadUserImageUseCase.invoke(imageUri).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _uploadUserImageState.value = UploadUserImageState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _uploadUserImageState.value = UploadUserImageState(data = it.data)

                    }

                    is ResultState.Error -> {
                        _uploadUserImageState.value = UploadUserImageState(error = it.error)
                    }
                }
            }
        }
    }

    fun clearUpdateProfileDataState() {
        _updateProfileDataState.value = SetProfileDataState()
        _uploadUserImageState.value = UploadUserImageState()
    }
}

data class GetProfileDataState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: UserModel? = null
)

data class SetProfileDataState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: String? = null
)

data class UploadUserImageState(
    val isLoading: Boolean = false,
    val error: String = "",
    val data: String? = null
)