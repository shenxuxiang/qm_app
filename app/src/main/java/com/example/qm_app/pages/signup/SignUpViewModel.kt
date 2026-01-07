package com.example.qm_app.pages.signup

import androidx.lifecycle.ViewModel
import com.example.qm_app.api.SignUpServiceApi
import com.example.qm_app.common.LogUntil
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.await
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {
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
}