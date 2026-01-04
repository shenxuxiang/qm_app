package com.example.qm_app.http

import com.example.qm_app.common.LogUntil
import com.example.qm_app.common.TokenManager
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
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
                var message: String? = null
                if (responseData is ResponseData<*>) {
                    when (responseData.code) {
                        0 -> { // 正常
                            continuation.resume(value = responseData)
                        }

                        401 -> { // 用户无权限
                            message = "用户暂未登录"
                            TokenManager.token = null
                            HttpToolkit.cancelAllPendingRequest()
                            Router.navigate(route = Route.LoginScreen.route) {
                                popUpTo(Route.HomeScreen.route) { inclusive = true }
                            }
                            continuation.resumeWithException(RuntimeException("请求已取消"))
                        }

                        else -> { // 其他异常
                            message = responseData.message
                            continuation.resumeWithException(RuntimeException(responseData.message))
                        }
                    }
                } else {
                    continuation.resume(value = responseData)
                }

                // 给用户弹出提示框
                if (message != null) Toast.postShowWarningToast(message)
            } else {
                val httpException = HttpException(response)
                val message = HttpToolkit.getBadResponseMsg(httpException)
                Toast.postShowWarningToast(message)
                continuation.resumeWithException(httpException)
            }
        }

        override fun onFailure(call: Call<T?>, t: Throwable) {
            if (call.isCanceled) {
                LogUntil.d(msg = "请求已被取消")
            } else {
                val msg = when (t) {
                    is ConnectException -> "网络异常"

                    is SocketTimeoutException -> "请求超时"

                    else -> t.message ?: "未知异常"
                }

                LogUntil.d(msg)
                Toast.postShowWarningToast(msg)
            }
            continuation.resumeWithException(t)
        }
    })
}




