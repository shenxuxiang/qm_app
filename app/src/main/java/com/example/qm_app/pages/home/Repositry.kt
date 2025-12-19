package com.example.qm_app.pages.home

import com.example.qm_app.api.MainServiceApi
import com.example.qm_app.http.HttpRequest

class Repositry {
    private val apiService = HttpRequest.create<MainServiceApi>()
}