package com.example.qm_app.pages.tab.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qm_app.api.HomeServiceApi
import com.example.qm_app.common.LogUntil
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabHomeViewModel @Inject constructor() : ViewModel() {
    private val apiService = HttpRequest.create<HomeServiceApi>()
    private val _uiState = MutableStateFlow(value = UiState())
    val uiState = _uiState.asStateFlow()

    fun saveScrollState(index: Int, offset: Int) { // 保存页面的滚动状态
        _uiState.update {
            it.copy(firstVisibleItemIndex = index, firstVisibleItemScrollOffset = offset)
        }
    }

    fun updateBannerIndex(index: Int) { // 更新轮播图 Index
        _uiState.update {
            it.copy(bannerIndex = index)
        }
    }

    fun queryBannerList() { // 获取轮播图列表
        viewModelScope.launch {
            try {
                val resp = apiService.queryBannerList(body = mapOf()).await()
                _uiState.update {
                    val list =
                        (resp.data as List<Map<*, *>>).map { item -> item["imageUrl"] as String }

                    it.copy(bannerList = list)
                }
            } catch (exception: Exception) {
                LogUntil.d(exception)
            }
        }
    }

    init {
        queryBannerList()
    }
}