package com.example.qm_app.api

import com.example.qm_app.entity.UserAddressData
import com.example.qm_app.http.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAddressServiceApi {
    @POST("v1.0/userAddress/page")
    fun queryUserAddressList(@Body body: Any): Call<ResponseData<ResponseList<UserAddressData>>>

    @POST("v1.0/userAddress/setDefault")
    fun setDefaultAddress(@Body body: Map<String, String>): Call<ResponseData<Any>>
}