package com.example.qm_app.pages.main

import androidx.compose.runtime.Stable

@Stable
class RegionSourceTree(
    val value: String,
    val label: String,
    val fullName: String,
    val children: List<RegionSourceTree>?,
)

@Stable
data class UiState(
    /**
     * 主页底部 NavBar，
     * 无论当前导航栈处于什么位置，都可以调用 mainViewModel.navToMainScreen(tab) 来切换到不同的 Tab
     * */
    val bottomMenusTabKey: String = MainTabBar.Home.route,

    /**
     * 获取省市区
     * */
    val regionData: List<RegionSourceTree> = emptyList(),
)