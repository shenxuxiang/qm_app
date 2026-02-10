package com.example.qm_app.pages.measure_land

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class Model1 @Inject constructor() : ViewModel() {
    private val _uiState1 = MutableStateFlow(UiState1())
    val uiState = _uiState1.asStateFlow()

    fun updateUIState(block: (UiState1) -> UiState1) {
        _uiState1.update { block(it) }
    }

    fun handleStart() {}

    fun handlePause() {}
}