package com.example.qm_app.api

import com.example.qm_app.http.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MainServiceApi {
    @POST("v1.0/banner/list")
    fun queryBannerList(@Body body: Map<String, Any>): Call<ResponseData<List<Any>>>

    @GET("v1.0/policyInformation/page")
    suspend fun queryNewsList(@Body body: Map<String, Any>): Call<ResponseData<List<Any>>>
}