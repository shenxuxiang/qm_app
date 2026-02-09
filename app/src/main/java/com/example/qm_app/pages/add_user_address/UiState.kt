package com.example.qm_app.pages.add_user_address

import androidx.compose.runtime.Stable
import com.example.qm_app.entity.SelectedOptionItem

@Stable
data class UiState(
    val userName: String = "",
    val phone: String = "",
    val location: String = "",
    val regions: List<SelectedOptionItem> = listOf(),
    val isDefault: Boolean = false,
)