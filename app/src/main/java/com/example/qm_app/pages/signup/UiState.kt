package com.example.qm_app.pages.signup

import androidx.compose.runtime.Stable
import com.example.qm_app.entity.SelectedOptionItem

@Stable
data class UiState(
    val checked: Boolean = false,
    val phone: String = "",
    val password: String = "",
    val username: String = "",
    val code: String = "",
    val regions: List<SelectedOptionItem> = listOf(),
)