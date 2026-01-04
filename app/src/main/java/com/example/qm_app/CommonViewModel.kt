package com.example.qm_app

import androidx.lifecycle.ViewModel
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
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
}

