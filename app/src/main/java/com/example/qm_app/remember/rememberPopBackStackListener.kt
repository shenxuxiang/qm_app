package com.example.qm_app.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.qm_app.router.Router
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 对 popBackStack 进行监听，当从source页面返回时触发 block 函数，泛型 T 表示返回的数据类型
 * @param source 表示从那个页面返回的
 * @param block  回调函数
 * */
@Composable
inline fun <reified T> rememberPopBackStackListener(
    source: String,
    crossinline block: (T) -> Unit,
) {
    val type = remember { object : TypeToken<Router.BackResult<T>>() {}.type }
    val backResult = remember {
        Router.controller.currentBackStackEntry!!.savedStateHandle.getStateFlow<String?>(
            "arguments",
            initialValue = null,
        )
    }

    LaunchedEffect(backResult) {
        backResult.collect {
            if (it != null) {
                val gson = Gson()
                val response = gson.fromJson<Router.BackResult<T>>(it, type)
                if (response.source == source) {
                    block(response.data)
                    Router.controller.currentBackStackEntry!!.savedStateHandle["arguments"] = null
                }
            }
        }
    }
}