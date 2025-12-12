package com.example.qm_app.pages.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StateHolder {
    data class ScrollState(
        val firstVisibleItemIndex: Int,
        val firstVisibleItemScrollOffset: Int,
    )

    private val _scrollState = MutableStateFlow(ScrollState(0, 0))
    val scrollState = _scrollState.asStateFlow()

    fun updateScrollState(itemIndex: Int, offset: Int) {
        _scrollState.value = ScrollState(itemIndex, offset)
    }
}