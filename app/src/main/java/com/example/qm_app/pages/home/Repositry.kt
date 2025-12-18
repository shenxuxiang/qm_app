package com.example.qm_app.pages.home

import com.example.qm_app.api.MainServiceApi
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.await

class Repositry {
    private val apiService = HttpRequest.create<MainServiceApi>()

    suspend fun getBannerList(body: Any) = apiService.queryBannerList(body).await()
}