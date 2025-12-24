package com.example.qm_app.pages.tab.service

import androidx.lifecycle.ViewModel
import com.example.qm_app.common.LogUntil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ServiceViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(value = UiState())
    val uiState = _uiState.asStateFlow()
    private val repository = Repository()

    suspend fun getBannerList(body: Map<String, Any>) {
        try {
            val resp = repository.queryBannerList(body)
            _uiState.update { it.copy(bannerList = resp.data) }
        } catch (exception: Exception) {
            LogUntil.d(msg = exception.message!!)
        }
    }

    suspend fun getNewsList(body: Map<String, Any>) {
        try {
            val resp = repository.queryNewsList(body)
            _uiState.update { it.copy(newsList = resp.data) }
        } catch (exception: Exception) {
            LogUntil.d(msg = exception.message!!)
        }
    }
}