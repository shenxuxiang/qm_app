package com.example.qm_app.pages.add_user_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qm_app.api.UserAddressServiceApi
import com.example.qm_app.common.LogUntil
import com.example.qm_app.components.loading.Loading
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.entity.UserAddressData
import com.example.qm_app.event_bus.UserAddressEventBus
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.await
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor() : ViewModel() {
    private val apiService = HttpRequest.create<UserAddressServiceApi>()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun updateUIState(block: (UiState) -> UiState) {
        _uiState.update {
            block(it)
        }
    }

    fun addAddress(params: Any) {
        viewModelScope.launch {
            Loading.show()
            try {
                apiService.addUserAddress(params).await()
                Loading.hide()
                Toast.showSuccessToast("添加成功，即将跳转至首页");
                delay(2000)
                Router.popBackStack(Route.MainScreen.route, inclusive = false, saveState = false)
            } catch (exception: Exception) {
                LogUntil.d(exception)
            }
        }
    }

    fun updateAddress(params: Any) {
        viewModelScope.launch {
            Loading.show()
            try {
                apiService.updateUserAddress(params).await()
                Loading.hide()
                Toast.showSuccessToast("修改成功~");

                // 通知列表页面更新当前地址的状态
                val gson = Gson()
                UserAddressEventBus.emit(
                    UserAddressEventBus.UiEvent.Update(
                        gson.fromJson(
                            params.toString(),
                            UserAddressData::class.java
                        )
                    )
                )

                delay(2000)
                Router.popBackStack()
            } catch (exception: Exception) {
                LogUntil.d(exception)
                Loading.hide()
            }
        }
    }

    fun queryUserAddressDetail(id: String) {
        viewModelScope.launch {
            try {
                val resp = apiService.queryUserAddressDetail(mapOf("id" to id)).await()
                val data = resp.data
                updateUIState {
                    it.copy(
                        isLoading = false,
                        phone = data.phone,
                        location = data.address,
                        userName = data.username,
                        isDefault = data.defaultFlag,
                        regions = arrayListOf(),
                    )
                }
            } catch (exception: Exception) {
                LogUntil.d(exception)
            }
        }
    }
}