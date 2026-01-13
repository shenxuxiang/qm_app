package com.example.qm_app.pages.signup

import androidx.lifecycle.ViewModel
import com.example.qm_app.api.SignUpServiceApi
import com.example.qm_app.common.LogUntil
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())

    val uiState = _uiState.asStateFlow()
    val apiService = HttpRequest.create<SignUpServiceApi>()

    /**
     * 发送登录验证
     * */
    suspend fun sendSMSAuthCode(phone: String) {
        try {
            apiService.sendSMSAuthCode(mapOf("phone" to phone, "type" to 2)).await()
            Toast.showSuccessToast("验证码已发送")
        } catch (exception: Exception) {
            LogUntil.d(exception)
        }
    }

    /**
     * 用户注册
     * */
    suspend fun authRegister(body: Map<String, Any>): Boolean {
        try {
            apiService.authRegister(body).await()
            Toast.showSuccessToast("注册成功")
            return true
        } catch (exception: Exception) {
            LogUntil.d(exception)
            return false
        }
    }

    fun updateState(black: (uiState: UiState) -> UiState) {
        _uiState.update {
            black(it)
        }
    }
}