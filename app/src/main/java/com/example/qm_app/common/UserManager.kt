package com.example.qm_app.common

import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import com.example.qm_app.entity.UserInfo
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object UserManager {
    private const val USER_INFO = "USER_INFO"
    private var _userInfo = MutableStateFlow<UserInfo?>(null)

    val userInfo = _userInfo.asStateFlow()

    init {
        val json = QmApplication.context.getSharedPreferences("prefs", MODE_PRIVATE)
            .getString(USER_INFO, "")

        if (json != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val gson = Gson()
                _userInfo.value = gson.fromJson(json, UserInfo::class.java)
            }
        }
    }

    /**
     * 更新用户信息
     * */
    fun updateUserInfo(value: Map<String, Any>) {
        val gson = Gson()
        val json = gson.toJson(value)
        _userInfo.value = gson.fromJson(json, UserInfo::class.java)
        QmApplication.context.getSharedPreferences("prefs", MODE_PRIVATE).edit {
            putString(USER_INFO, json)
        }
    }
}