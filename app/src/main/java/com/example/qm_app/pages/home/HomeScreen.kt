package com.example.qm_app.pages.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
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
import com.example.qm_app.pages.home.components.BottomNavBar
import com.example.qm_app.pages.tab.agricultural.AgriculturalScreen
import com.example.qm_app.pages.tab.home.TabHomeScreen
import com.example.qm_app.pages.tab.mine.MineScreen
import com.example.qm_app.pages.tab.release.ReleaseScreen
import com.example.qm_app.pages.tab.service.ServiceScreen

val tabItems = listOf(
    HomeTabBar.Home,
    HomeTabBar.Service,
    HomeTabBar.Release,
    HomeTabBar.AgriculturalTechnology,
    HomeTabBar.Mine,
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    val commonViewModel = QmApplication.commonViewModel
    val uiState by commonViewModel.uiState.collectAsState()
    val state = rememberPagerState(
        initialPage = 0,
        pageCount = { tabItems.size }
    )

    LaunchedEffect(uiState.bottomMenusTabKey) {
        val index = tabItems.indexOfFirst { screen -> screen.route == uiState.bottomMenusTabKey }
        if (state.currentPage != index) state.scrollToPage(index)
    }

    LaunchedEffect(state.currentPage) {
        val screen = tabItems[state.currentPage]
        if (screen.route != uiState.bottomMenusTabKey) commonViewModel.navToMainScreen(tab = screen.route)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        bottomBar = {
            BottomNavBar(
                tabKey = uiState.bottomMenusTabKey,
                items = tabItems,
            ) { tab ->
                commonViewModel.navToMainScreen(tab)
            }
        },
        contentWindowInsets = WindowInsets.navigationBars,
    ) { paddingValues ->
        HorizontalPager(
            state = state,
            pageSize = PageSize.Fill,
            beyondViewportPageCount = tabItems.size,
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

