package com.example.qm_app.pages.home

import androidx.annotation.StringRes
import com.example.qm_app.R
import com.example.qm_app.common.QmIcons

sealed class HomeTabBar(
    val route: String,
    val icon: QmIcons,
    val selectedIcon: QmIcons,
    @StringRes val resourceId: Int,
) {
    object Home : HomeTabBar(
        route = "home",
        resourceId = R.string.home,
        icon = QmIcons.Stroke.TabBarMenuHome,
        selectedIcon = QmIcons.Filled.TabBarMenuHome,
    )

    object Service : HomeTabBar(
        route = "service",
        resourceId = R.string.service,
        icon = QmIcons.Stroke.TabBarMenuService,
        selectedIcon = QmIcons.Filled.TabBarMenuService,
    )

    object Release : HomeTabBar(
        route = "release",
        resourceId = R.string.release,
        icon = QmIcons.Stroke.TabBarMenuRelease,
        selectedIcon = QmIcons.Filled.TabBarMenuRelease,
    )

    object AgriculturalTechnology : HomeTabBar(
        route = "agricultural_technology",
        resourceId = R.string.agricultural_technology,
        icon = QmIcons.Stroke.TabBarMenuAgricultural,
        selectedIcon = QmIcons.Filled.TabBarMenuAgricultural,
    )

    object Mine : HomeTabBar(
        route = "mine",
        resourceId = R.string.mine,
        icon = QmIcons.Stroke.TabBarMenuMine,
        selectedIcon = QmIcons.Filled.TabBarMenuMine,
    )
}