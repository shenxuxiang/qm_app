package com.example.qm_app.http

import com.example.qm_app.common.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class HttpInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest =
            request.newBuilder()
                .header(name = "Authorization", value = "Bearer ${TokenManager.token}")
                .build()

        return chain.proceed(newRequest)
    }
}