package com.example.qm_app.api

import com.example.qm_app.http.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginServiceApi {
    /**
     * 获取登录验证码
     * */
    @POST("v1.0/auth/sms/send")
    fun sendSMSAuthCode(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<ResponseData<Boolean>>

    /**
     * 验证码登录
     * */
    @POST("v1.0/auth/login/phoneCode")
    fun authLoginForPhoneCode(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<ResponseData<Map<String, Any>>>
}