package com.example.qm_app.pages.user_auth_farm_view

import androidx.compose.runtime.Stable

@Stable
data class UiState(
    val loading: Boolean = true,
    val status: Int? = null,
    val userTypeName: String = "",
    val checkStatusName: String = "",
    val checkRemark: String = "",
    val nationalEmblemUrl: String = "",
    val portraitUrl: String = "",
    val handHeldUrl: String = "",
    val realName: String = "",
    val idCardNum: String = "",
    val validDate: String = "",
)