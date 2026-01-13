package com.example.qm_app.common

import android.util.Base64
import java.security.MessageDigest

object Encrypter {
    fun encrypt(input: String): String {
        val digest = MessageDigest.getInstance("SHA-512")
        val bytes = digest.digest(input.toByteArray())
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}