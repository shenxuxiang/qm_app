package com.example.qm_app

import androidx.lifecycle.ViewModel
import com.example.qm_app.components.loading.Loading
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.utils.loadAssetsFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(value = UiState())
    val uiState = _uiState.asStateFlow()

    /**
     * 导航到主页下指定的 Tab 页
     * */
    fun navToMainScreen(tab: String) {
        _uiState.update { it.copy(bottomMenusTabKey = tab) }
        if (Router.controller.currentBackStackEntry?.destination?.route != Route.HomeScreen.route) {
            Router.navigate(Route.HomeScreen.route) {
                popUpTo(Route.HomeScreen.route) {}
                launchSingleTop = true
            }
        }
    }

    /**
     * 获取省市区
     * */
    suspend fun queryRegionData() {
        Loading.show()
        var isOk = true
        try {
            val json = loadAssetsFile("region_data.json")
            val gson = Gson()
            val type = object : TypeToken<List<RegionData>>() {}.type
            _uiState.update {
                it.copy(regionData = gson.fromJson(json, type))
            }

        } catch (e: Exception) {
            isOk = false
        } finally {
            Loading.hide()
            if (!isOk) Toast.showWarningToast("数据加载失败")
        }
    }
}

