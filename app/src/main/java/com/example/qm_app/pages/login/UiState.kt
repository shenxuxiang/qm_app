package com.example.qm_app.pages.login

import androidx.compose.runtime.Stable

@Stable
data class UiState(
    val tabIndex: Int = 0,
    val phone: String = "",
    val code: String = "",
    val account: String = "",
    val password: String = "",
    val checkedOfFast: Boolean = false,
    val checkedOfAccount: Boolean = false,
)