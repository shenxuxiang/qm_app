package com.example.qm_app.compositionLocal

import androidx.compose.runtime.compositionLocalOf
import com.example.qm_app.entity.UserInfo

val LocalUserInfo = compositionLocalOf<UserInfo?> { null }