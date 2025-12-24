package com.example.qm_app.pages.tab.service

data class UiState(
    val isLoading: Boolean = true,
    val newsList: List<Any> = emptyList(),
    val bannerList: List<Any> = emptyList(),
)