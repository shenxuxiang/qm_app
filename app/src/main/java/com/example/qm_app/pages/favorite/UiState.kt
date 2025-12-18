package com.example.qm_app.pages.favorite

data class UiState(
    val isLoading: Boolean = true,
    val newsList: List<Any> = emptyList(),
    val bannerList: List<Any> = emptyList(),
)