package com.example.qm_app.pages.favorite

import com.example.qm_app.api.MainServiceApi
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.await

class Repository {
    val apiService = HttpRequest.create<MainServiceApi>()

    // 挂起函数可以返回值
    suspend fun getBannerList(body: Any) = apiService.queryBannerList(body).await()
}