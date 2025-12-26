package com.example.qm_app.common

import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

object TokenManager {
    private const val TOKEN = "_Token_"

    private fun _getToken(): String? {
        return QmApplication.context.getSharedPreferences("prefs", MODE_PRIVATE)
            .getString(TOKEN, null)
    }

    private fun _setToken(newValue: String?) {
        QmApplication.context.getSharedPreferences("prefs", MODE_PRIVATE).edit {
            if (newValue == null) {
                remove(TOKEN)
            } else {
                putString(TOKEN, newValue)
            }
        }
    }

    var token: String?
        get() = _getToken()
        set(newValue) = _setToken(newValue)
}