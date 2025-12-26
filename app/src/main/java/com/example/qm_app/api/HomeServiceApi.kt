package com.example.qm_app.api

import com.example.qm_app.http.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class NewsItem(
    val title: String,
    val id: String,
    val typeName: String,
    val source: String,
    val updater: String,
    val updateTime: String,
    val status: Int,
    val statusName: String,
    val primaryUrl: String,
)

data class ResponseList<T>(val list: List<T>, val pageSize: Int, val pageNum: Int, val total: Int)

interface HomeServiceApi {
    @POST("v1.0/banner/list")
    fun queryBannerList(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<ResponseData<List<Any>>>


    @POST("v1.0/policyInformation/page")
    fun queryNewsList(@Body body: Map<String, @JvmSuppressWildcards Any>): Call<ResponseData<ResponseList<NewsItem>>>
}