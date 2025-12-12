package com.example.qm_app.pages.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.qm_app.common.QmApplication
import com.example.qm_app.components.BottomNavBar
import com.example.qm_app.pages.cart.CartScreen
import com.example.qm_app.pages.favorite.FavoriteScreen
import com.example.qm_app.pages.goods.GoodsScreen
import com.example.qm_app.pages.home.HomeScreen

val items = listOf(
    Screen.Home,
    Screen.Favorite,
    Screen.Cart,
    Screen.Profile,
)

@Composable
fun MainScreen() {
    val commonViewModel = QmApplication.commonViewModel
    val bottomNavBarTab = commonViewModel.bottomNavBarTab.collectAsState()
    val state = rememberPagerState(
        initialPage = 0,
        pageCount = { items.size })

    LaunchedEffect(bottomNavBarTab.value) {
        val index = items.indexOfFirst { screen -> screen.route == bottomNavBarTab.value }
        if (state.currentPage != index) state.scrollToPage(index)
    }

    LaunchedEffect(state.currentPage) {
        val screen = items[state.currentPage]
        if (screen.route != bottomNavBarTab.value) commonViewModel.navToMainScreen(tab = screen.route)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            BottomNavBar(
                tabKey = bottomNavBarTab.value,
                items = items,
            ) { tab ->
                commonViewModel.navToMainScreen(tab)
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            state = state,
            pageSize = PageSize.Fill,
        ) { tab ->
            when (tab) {
                0 -> HomeScreen(paddingValues)
                1 -> FavoriteScreen()
                2 -> CartScreen()
                3 -> GoodsScreen()
            }
        }
    }
}