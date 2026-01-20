package com.example.qm_app.pages.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.qm_app.common.QmApplication
import com.example.qm_app.pages.main.components.BottomNavBar
import com.example.qm_app.pages.tab.agricultural.AgriculturalScreen
import com.example.qm_app.pages.tab.home.TabHomeScreen
import com.example.qm_app.pages.tab.mine.MineScreen
import com.example.qm_app.pages.tab.release.ReleaseScreen
import com.example.qm_app.pages.tab.service.ServiceScreen

/**
 * 底部导航栏菜单项，菜单顺序固定，不要改动
 * */
val tabItems = listOf(
    MainTabBar.Home,
    MainTabBar.Service,
    MainTabBar.Release,
    MainTabBar.AgriculturalTechnology,
    MainTabBar.Mine,
)

@Composable
fun MainScreen() {
    val mainViewModel = QmApplication.mainViewModel
    val uiState by mainViewModel.uiState.collectAsState()
    val state = rememberPagerState(
        pageCount = { tabItems.size },
        initialPage = tabItems.indexOfFirst { screen -> screen.route == uiState.bottomMenusTabKey },
    )

    LaunchedEffect(uiState.bottomMenusTabKey) {
        val index = tabItems.indexOfFirst { screen -> screen.route == uiState.bottomMenusTabKey }
        if (state.currentPage != index) state.scrollToPage(index)
    }

    LaunchedEffect(state.currentPage) {
        val screen = tabItems[state.currentPage]
        if (screen.route != uiState.bottomMenusTabKey) mainViewModel.navToMainScreen(tab = screen.route)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        bottomBar = {
            BottomNavBar(
                items = tabItems,
                tabKey = uiState.bottomMenusTabKey,
            ) { tab ->
                mainViewModel.navToMainScreen(tab)
            }
        },
        contentWindowInsets = WindowInsets(), // 一个空的 WindowInsets，不要使用默认值
    ) { paddingValues ->
        HorizontalPager(
            state = state,
            pageSize = PageSize.Fill,
            beyondViewportPageCount = 1,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) { tab ->
            when (tab) {
                0 -> TabHomeScreen()
                1 -> ServiceScreen()
                2 -> ReleaseScreen()
                3 -> AgriculturalScreen()
                4 -> MineScreen()
            }
        }
    }
}

