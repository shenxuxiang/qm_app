package com.example.qm_app.pages.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qm_app.common.TokenManager
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.utils.loadAssetsFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 主页的 MainViewModel 是全局共享的，所以它将会绑定到 QmApplication 下面，可以被所有页面共享。
 * */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(value = UiState())
    val uiState = _uiState.asStateFlow()

    /**
     * 导航到主页下指定的 Tab 页
     * */
    fun navToMainScreen(tab: String) {
        if (tab == MainTabBar.Mine.route && TokenManager.token == null) {
            Toast.postShowWarningToast("用户未登录")
            Router.navigate(Route.LoginScreen.route) {
                popUpTo(Route.MainScreen.route) { inclusive = true }
            }

            return
        }

        _uiState.update { it.copy(bottomMenusTabKey = tab) }
        if (Router.controller.currentBackStackEntry?.destination?.route != Route.MainScreen.route) {
            Router.navigate(Route.MainScreen.route) {
                popUpTo(Route.MainScreen.route) {}
                launchSingleTop = true
            }
        }
    }

    /**
     * 获取省市区
     * */
    fun queryRegionData() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val regionData = loadAssetsFile("region_data.json")
                _uiState.update {
                    it.copy(regionData = regionData)
                }
            } catch (e: Exception) {
                Toast.showWarningToast("数据加载失败")
            }
        }
    }
}

