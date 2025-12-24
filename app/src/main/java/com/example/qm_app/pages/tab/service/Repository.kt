package com.example.qm_app.pages.tab.service

import com.example.qm_app.api.MainServiceApi
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.await

class Repository {
    val apiService = HttpRequest.create<MainServiceApi>()

    // 挂起函数可以返回值
    suspend fun queryBannerList(body: Map<String, Any>) = apiService.queryBannerList(body).await()

    suspend fun queryNewsList(body: Map<String, Any>) = apiService.queryNewsList(body).await()
}