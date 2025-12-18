package com.example.qm_app.http

data class ResponseData<T>(val code: Int, val message: String, val data: T)