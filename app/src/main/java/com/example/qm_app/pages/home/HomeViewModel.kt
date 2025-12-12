package com.example.qm_app.pages.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _stateHolder = StateHolder()

    val uiState get() = _stateHolder

    fun updateScrollState(itemIndex: Int, offset: Int) =
        uiState.updateScrollState(itemIndex, offset)
}