package com.example.qm_app.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpRequest {
    private const val BASE_URL = "http://60.169.69.3:30062"
    private const val TIMEOUT = 60L

    val interceptor = HttpInterceptor()

    var client = HttpClient.create(TIMEOUT, interceptor)
        private set

    private val retrofit = Retrofit.Builder().run {
        baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> create(service: Class<T>): T = retrofit.create(service)

    /**
     * 创建 Service
     * */
    inline fun <reified T> create(): T = create(T::class.java)
}

