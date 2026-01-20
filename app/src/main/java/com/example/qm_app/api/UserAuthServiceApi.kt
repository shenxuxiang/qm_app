package com.example.qm_app.api

import com.example.qm_app.http.ResponseData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class UserIDCardInformation(
    val url: String,
    val name: String,
    val idCardNum: String,
    val validDate: String,
)

data class FarmCheckInformation(
    val userTypeName: String,
    val checkStatusName: String,
    val checkRemark: String,
    val nationalEmblemUrl: String,
    val portraitUrl: String,
    val handHeldUrl: String,
    val realName: String,
    val idCardNum: String,
    val validDate: String,
    val status: Int?,
)

interface UserAuthServiceApi {
    @Multipart
    @POST("v1.0/cardOcr/idCardBack")
    fun uploadIDCardBackFace(@Part query: MultipartBody.Part): Call<ResponseData<UserIDCardInformation>>

    @Multipart
    @POST("v1.0/cardOcr/idCardFace")
    fun uploadIDCardFrontFace(@Part query: MultipartBody.Part): Call<ResponseData<UserIDCardInformation>>

    @Multipart
    @POST("v1.0/file/upload")
    fun uploadFile(@Part query: MultipartBody.Part): Call<ResponseData<Map<String, Any>>>

    @POST("v1.0/farmerCheck/submit")
    fun submitFarmAuth(@Body query: Map<String, String>): Call<ResponseData<Any>>

    @POST("v1.0/farmerCheck/app/farmerCheckInfo")
    fun queryFarmCheckInfo(@Body query: Map<String, String>): Call<ResponseData<FarmCheckInformation>>
}