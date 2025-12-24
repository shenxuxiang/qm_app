package com.example.qm_app.api

import com.example.qm_app.http.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface HomeServiceApi {
    @POST("v1.0/banner/list")
    fun queryBannerList(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<ResponseData<List<Any>>>
}