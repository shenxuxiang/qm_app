package com.example.qm_app.api

import com.example.qm_app.entity.UserAddressData
import com.example.qm_app.http.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAddressServiceApi {
    @POST("v1.0/userAddress/page") // 用户地址列表
    fun queryUserAddressList(@Body body: Any): Call<ResponseData<ResponseList<UserAddressData>>>

    @POST("v1.0/userAddress/setDefault") // 设置为默认地址
    fun setDefaultAddress(@Body body: Map<String, String>): Call<ResponseData<Any>>

    @POST("v1.0/userAddress/add") // 新增地址
    fun addUserAddress(@Body body: Any): Call<ResponseData<Any>>

    @POST("v1.0/userAddress/update") // 更新地址
    fun updateUserAddress(@Body body: Any): Call<ResponseData<Any>>

    @POST("/v1.0/userAddress/detail")
    fun queryUserAddressDetail(@Body body: Map<String, String>): Call<ResponseData<UserAddressData>>
}