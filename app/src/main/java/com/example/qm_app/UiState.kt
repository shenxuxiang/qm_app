package com.example.qm_app

import androidx.compose.runtime.Stable
import com.example.qm_app.pages.home.HomeTabBar

@Stable
data class UiState(
    /**
     * 主页底部 NavBar，
     * 无论当前导航栈处于什么位置，都可以调用 commonViewModel.navToMainScreen(tab) 来切换到不同的 Tab
     * */
    val bottomMenusTabKey: String = HomeTabBar.Home.route,
)