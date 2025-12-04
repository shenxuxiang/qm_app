package com.example.qm_app.pages.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    ViewModel() {
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