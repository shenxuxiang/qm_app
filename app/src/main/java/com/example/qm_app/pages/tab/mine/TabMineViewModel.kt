package com.example.qm_app.pages.tab.mine

import androidx.lifecycle.ViewModel
import com.example.qm_app.api.MineServiceApi
import com.example.qm_app.http.HttpRequest
import com.example.qm_app.http.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TabMineViewModel @Inject constructor() : ViewModel() {
    val serviceApi = HttpRequest.create<MineServiceApi>()
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    /**
     * 更新 UI 状态
     * */
    fun updateUIState(black: (UiState) -> UiState) {
        _uiState.update {
            black(it)
        }
    }

    /**
     * 更新【用户认证状态】
     * */
    suspend fun queryUserCheckStatus() {
        try {
            val resp = serviceApi.queryUserCheckStatus().await()
            updateUIState {
                it.copy(userCheckStatus = resp.data)
            }
        } catch (t: Throwable) {
            println(t.message)
            t.printStackTrace()
        }
    }
}