package com.tech.indiaekartshoppinguser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.indiaekartshoppinguser.common.ResultState
import com.tech.indiaekartshoppinguser.domain.models.ShippingModel
import com.tech.indiaekartshoppinguser.domain.usecase.ShippingUseCase
import com.tech.indiaekartshoppinguser.presentation.screens.shipping.stateScreen.ShippingScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShippingViewmodel @Inject constructor(
    private val shippingUseCase: ShippingUseCase
): ViewModel() {

    private val _shippingScreenState = MutableStateFlow(ShippingScreenState())
    val shippingScreenState = _shippingScreenState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ShippingScreenState()
    )
    private val _shippingState = MutableStateFlow(ShippingState())
    val shippingState = _shippingState.asStateFlow()

    private val _getShippingState = MutableStateFlow(GetShippingDataState())
    val getShippingState = _getShippingState.asStateFlow()

    fun shippingDataSave(shippingModel: ShippingModel){
        viewModelScope.launch {
            shippingUseCase.invoke(shippingModel).collect {
                when(it){
                    is ResultState.Loading -> {
                        _shippingState.value = ShippingState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _shippingState.value = ShippingState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _shippingState.value = ShippingState(error = it.error)
                    }
                }
            }
        }
    }
    fun getShippingDataThroughUID(){
        viewModelScope.launch {
            shippingUseCase.invoke().collect{
                when(it){
                    is ResultState.Loading -> {
                        _getShippingState.value = GetShippingDataState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getShippingState.value = GetShippingDataState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _getShippingState.value = GetShippingDataState(error = it.error)
                    }
                }
            }
        }
    }
}
data class ShippingState(
    val isLoading : Boolean = false,
    val data : String = "",
    val error : String = ""
)
data class GetShippingDataState(
    val isLoading : Boolean = false,
    val data : ShippingModel? = null,
    val error : String = ""
)