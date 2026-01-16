package com.example.qm_app.api

import com.example.qm_app.entity.UserCheckStatus
import com.example.qm_app.http.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MineServiceApi {
    @POST("v1.0/sysUser/getCheckStatus")
    fun queryUserCheckStatus(
        @Body body: Map<String, String> = emptyMap(),
    ): Call<ResponseData<UserCheckStatus>>
}