package com.example.qm_app.pages.user_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qm_app.api.UserAddressServiceApi
import com.example.qm_app.common.LogUntil
import com.example.qm_app.components.loading.Loading
import com.example.qm_app.entity.UserAddressData
import com.example.qm_app.event_bus.UserAddressEventBus
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAddressViewModel @Inject constructor() : ViewModel() {
    private val serviceApi = HttpRequest.create<UserAddressServiceApi>()
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun updateUIState(block: (UiState) -> UiState) {
        _uiState.update { block(it) }
    }

    fun queryUserAddressList(pageNum: Int, pageSize: Int) {
        viewModelScope.launch {
            try {
                val body = mapOf(
                    "pageNum" to pageNum,
                    "pageSize" to pageSize,
                )
                val resp = serviceApi.queryUserAddressList(body).await()
                val newList = arrayListOf<UserAddressData>()
                if (pageNum == 1) {
                    newList.addAll(resp.data.list)
                } else {
                    newList.addAll(uiState.value.addressList)
                    newList.addAll(resp.data.list)
                }
                updateUIState {
                    it.copy(
                        pageNum = pageNum,
                        pageSize = pageSize,
                        addressList = newList,
                        total = resp.data.total,
                        defaultAddress = newList.find { address -> address.defaultFlag },
                    )
                }
            } catch (exception: Exception) {
                LogUntil.d(exception)
            }
        }
    }

    /**
     * 设置为默认地址
     * */
    fun setDefaultAddress(address: UserAddressData) {
        viewModelScope.launch {
            try {
                Loading.show()
                serviceApi.setDefaultAddress(mapOf("addressId" to address.addressId)).await()
                updateUIState { it.copy(defaultAddress = address) }
            } catch (exception: Exception) {
                LogUntil.d(exception)
            } finally {
                Loading.hide()
            }
        }
    }

    init {
        val pageNum = uiState.value.pageNum
        val pageSize = uiState.value.pageSize
        queryUserAddressList(pageNum, pageSize)

        // 监听用户地址的变化（修改）
        // 这里的协程不需要手动取消，当 ViewModel 销毁时自动取消该 viewModelScope 启动的协程
        viewModelScope.launch {
            UserAddressEventBus.event.collect {
                if (it is UserAddressEventBus.UiEvent.Update) {
                    val data = it.data
                    val index =
                        _uiState.value.addressList.indexOfFirst { item -> item.addressId == data.addressId }


                    if (index >= 0) {
                        val newAddressList = arrayListOf<UserAddressData>()
                        newAddressList.addAll(_uiState.value.addressList)
                        newAddressList[index] = data

                        updateUIState { ui ->
                            ui.copy(
                                addressList = newAddressList,
                                defaultAddress = if (data.defaultFlag) data else ui.defaultAddress
                            )
                        }
                    }
                }
            }
        }
    }
}