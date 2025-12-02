package com.example.qm_app.pages.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    ViewModel() {
    data class ScrollState(
        val firstVisibleItemIndex: Int,
        val firstVisibleItemScrollOffset: Int,
    )

    init {
        val resp = savedStateHandle.getStateFlow<String?>("ResponseData", null)

        viewModelScope.launch {
            resp.collect {
                println("========================1 resp: ${resp.value}")
            }
        }
    }

    private val _scrollState = MutableStateFlow(ScrollState(0, 0))
    val scrollState = _scrollState.asStateFlow()

    fun updateScrollState(itemIndex: Int, offset: Int) {
        _scrollState.value = ScrollState(itemIndex, offset)
    }
}