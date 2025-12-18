package com.example.qm_app.http

import com.example.qm_app.common.LogUntil
import com.example.qm_app.common.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Call<T>.await(): T = suspendCoroutine { continuation ->
    enqueue(object : Callback<T> {
        /**
         * 服务器响应成功，不论 status 是否等于 200，都会触发 onResponse 的回调。
         * 所以在回调中，先要判断 response.isSuccessful 是否为 true，然后再对 response data 进行进一步判断。
         * 当 response.isSuccessful == false 时，创建一个 HttpException 的异常对象并返回。
         * */
        override fun onResponse(call: Call<T>, response: Response<T>) {
            // 必须要先判断响应是否成功
            if (response.isSuccessful) {
                val responseData = response.body()!!

                if (responseData is ResponseData<*>) {
                    when (responseData.code) {
                        0 -> { // 正常
                            continuation.resume(value = responseData)
                        }

                        401 -> { // 用户无权限
                            TokenManager.token = null
                            HttpToolkit.cancelAllPendingRequest()
                            continuation.resumeWithException(RuntimeException("请求已取消"))
                        }

                        else -> { // 其他异常
                            LogUntil.d("HttpRequest", responseData.message)
                            continuation.resumeWithException(RuntimeException(responseData.message))
                        }
                    }
                } else {
                    continuation.resume(value = responseData)
                }
            } else {
                val httpException = HttpException(response)
                val message = HttpToolkit.getBadResponseMsg(httpException)
                LogUntil.d("HttpRequest", message)
                continuation.resumeWithException(httpException)
            }
        }

        override fun onFailure(call: Call<T?>, t: Throwable) {
            if (call.isCanceled) {
                LogUntil.d("HttpRequest", "请求已被取消")
            } else {
                when (t) {
                    is ConnectException -> {
                        LogUntil.d("HttpRequest", "网络异常")
                    }

                    is SocketTimeoutException -> {
                        LogUntil.d("HttpRequest", "请求超时")
                    }

                    else -> {
                        LogUntil.d("HttpRequest", t.message ?: "未知异常")
                    }
                }
            }

            for (item in t.stackTrace) {
                LogUntil.d("HttpRequest", item.toString())
            }
            continuation.resumeWithException(t)
        }
    })
}




