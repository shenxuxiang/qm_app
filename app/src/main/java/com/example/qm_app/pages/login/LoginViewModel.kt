package com.example.qm_app.pages.login

import androidx.lifecycle.ViewModel
import com.example.qm_app.api.LoginServiceApi
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.ResponseData
import com.example.qm_app.http.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val apiService = HttpRequest.create<LoginServiceApi>()
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    /**
     * 切换【快速登录/账号登录】
     * */
    fun updateTabIndex(index: Int) {
        _uiState.update {
            it.copy(tabIndex = index)
        }
    }

    fun updateUIState(block: (UiState) -> UiState) {
        _uiState.update {
            block(it)
        }
    }

    /**
     * 输入手机号码
     * */
    fun changeUserPhone(input: String) {
        _uiState.update {
            it.copy(phone = input)
        }
    }

    /**
     * 输入验证码
     * */
    fun changeUserCode(input: String) {
        _uiState.update {
            it.copy(code = input)
        }
    }

    /**
     * 发送登录验证
     * */
    suspend fun sendSMSAuthCode(phone: String, type: Int) {
        try {
            apiService.sendSMSAuthCode(mapOf("phone" to phone, "type" to type)).await()
            Toast.showSuccessToast("验证码已发送")
        } catch (_: Exception) {
        }
    }

    /**
     * 验证码登录
     * */
    suspend fun authLoginForPhoneCode(
        phone: String,
        code: String,
    ): ResponseData<Map<String, Any>>? {
        return try {
            apiService.authLoginForPhoneCode(mapOf("phone" to phone, "code" to code)).await()
        } catch (_: Exception) {
            null
        }
    }

    /**
     * 密码登录
     * */
    suspend fun authLogin(account: String, password: String): ResponseData<Map<String, Any>>? {
        return try {
            apiService.authLogin(
                body = mapOf(
                    "username" to account,
                    "password" to password,
                    "clientType" to "app",
                ),
            ).await()
        } catch (_: Exception) {
            null
        }
    }
}