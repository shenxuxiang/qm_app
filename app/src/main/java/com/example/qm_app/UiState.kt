package com.example.qm_app

import androidx.compose.runtime.Stable
import com.example.qm_app.pages.home.HomeTabBar

@Stable
class RegionData(
    val value: String,
    val label: String,
    val fullName: String,
    val children: List<RegionData>,
)

@Stable
data class UiState(
    /**
     * 主页底部 NavBar，
     * 无论当前导航栈处于什么位置，都可以调用 commonViewModel.navToMainScreen(tab) 来切换到不同的 Tab
     * */
    val bottomMenusTabKey: String = HomeTabBar.Home.route,

    /**
     * 获取省市区
     * */
    val regionData: List<RegionData> = emptyList(),
)