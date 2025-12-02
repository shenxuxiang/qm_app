package com.example.qm_app.pages.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.qm_app.common.QmApplication
import com.example.qm_app.components.BottomNavBar
import com.example.qm_app.pages.cart.CartScreen
import com.example.qm_app.pages.favorite.FavoriteScreen
import com.example.qm_app.pages.home.HomeScreen
import com.example.qm_app.pages.profile.ProfileScreen

val items = listOf(
    Screen.Home,
    Screen.Favorite,
    Screen.Cart,
    Screen.Profile,
)

@Composable
fun MainScreen(navController: NavController) {
    val commonViewModel = QmApplication.commonViewModel
    val bottomNavBarTab = commonViewModel.bottomNavBarTab.collectAsState()

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
        when (bottomNavBarTab.value) {
            Screen.Home.route -> HomeScreen(navController, paddingValues)
            Screen.Favorite.route -> FavoriteScreen(navController)
            Screen.Cart.route -> CartScreen(navController)
            Screen.Profile.route -> ProfileScreen(navController)
        }
    }
}