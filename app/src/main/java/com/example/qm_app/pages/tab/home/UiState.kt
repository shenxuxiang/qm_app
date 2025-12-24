package com.example.qm_app.pages.tab.home

import androidx.compose.runtime.Stable

@Stable
data class UiState(
    val firstVisibleItemIndex: Int = 0,
    val firstVisibleItemScrollOffset: Int = 0,
    
    val bannerList: List<String> = emptyList(),
    val bannerIndex: Int = 0,
)