package com.example.qm_app.pages.user_auth_farm

import android.graphics.Bitmap
import androidx.compose.runtime.Stable

@Stable
data class UiState(
    val bitmap1: Bitmap? = null,
    val bitmap2: Bitmap? = null,
    val bitmap3: Bitmap? = null,
    val userName: String = "",
    val idNumber: String = "",
    val dateTime: String = "",
)