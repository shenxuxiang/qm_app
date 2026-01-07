package com.example.qm_app.api

import com.example.qm_app.http.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpServiceApi {
    /**
     * 获取注册验证码
     * */
    @POST("v1.0/auth/sms/send")
    fun sendSMSAuthCode(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<ResponseData<Boolean>>

    /**
     * 注册
     * */
    @POST("v1.0/auth/register")
    fun authRegister(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<ResponseData<Any>>
}
