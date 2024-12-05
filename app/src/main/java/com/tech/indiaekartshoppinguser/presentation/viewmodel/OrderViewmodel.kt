package com.tech.indiaekartshoppinguser.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.indiaekartshoppinguser.common.ResultState
import com.tech.indiaekartshoppinguser.domain.models.OrderModel
import com.tech.indiaekartshoppinguser.domain.usecase.OrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewmodel @Inject constructor(
    private val orderUseCase: OrderUseCase
): ViewModel() {

    private val _orderState = MutableStateFlow(OrderState())
    val orderState = _orderState.asStateFlow()

    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState = _paymentState.asStateFlow()

    private val _getAllOrderDataState = MutableStateFlow(GetAllOrderDataState())
    val getAllOrderDataState = _getAllOrderDataState.asStateFlow()


    fun orderDataSave(orderList: List<OrderModel>){
        viewModelScope.launch {
            orderUseCase.invoke(orderList).collect {
                when(it){
                    is ResultState.Loading -> {
                        _orderState.value = OrderState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _orderState.value = OrderState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _orderState.value = OrderState(error = it.error)
                    }
                }
            }
        }
    }
    fun setPaymentState(paymentId : String = "",errorMsg: String = "") {
        if(errorMsg.isEmpty()) {
            _paymentState.value = PaymentState(
                isLoading = false,
                paymentState = paymentId,
                error = ""
            )
        }else{
            _paymentState.value = PaymentState(
                isLoading = false,
                paymentState = "",
                error = errorMsg
            )
        }
    }
    fun getAllOrderState(){
        viewModelScope.launch {
            orderUseCase.invoke().collect{
                when(it) {
                    is ResultState.Loading -> {
                        _getAllOrderDataState.value = GetAllOrderDataState(isLoading = true)
                    }
                    is ResultState.Success -> {
                        _getAllOrderDataState.value = GetAllOrderDataState(data = it.data)
                    }
                    is ResultState.Error -> {
                        _getAllOrderDataState.value = GetAllOrderDataState(error = it.error)
                    }
                }
            }
        }
    }
    fun clearOrderState(){
        _orderState.value = OrderState()
    }
    fun clearPaymentState(){
        _paymentState.value = PaymentState()
    }
}
data class OrderState(
    val isLoading : Boolean = false,
    val data : String = "",
    val error : String = ""
)
data class PaymentState(
    val isLoading : Boolean = false,
    val paymentState : String = "",
    val error : String = ""
)
data class GetAllOrderDataState(
    val isLoading : Boolean = false,
    val data : List<OrderModel> = emptyList(),
    val error : String = ""
)