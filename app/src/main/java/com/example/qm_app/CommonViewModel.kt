package com.example.qm_app

import androidx.lifecycle.ViewModel
import com.example.qm_app.common.QmApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor() : ViewModel() {
    private val _bottomNavBarTabKey = MutableStateFlow("home")

    /**
     * 主页底部 NavBar，
     * 无论当前导航栈处于什么位置，都可以调用 commonViewModel.navToMainScreen(tab) 来切换到不同的 Tab
     * */
    val bottomNavBarTab = _bottomNavBarTabKey.asStateFlow()

    fun navToMainScreen(tab: String) {
        _bottomNavBarTabKey.value = tab
        val navController = QmApplication.navController
        if (navController.currentBackStackEntry?.destination?.route != "main") {
            navController.navigate("main") {
                popUpTo("main") {}
                launchSingleTop = true
            }
        }
    }
}

