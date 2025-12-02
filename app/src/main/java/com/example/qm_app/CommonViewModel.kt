package com.example.qm_app

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor() :
    ViewModel() {

    private var _navController = MutableStateFlow<NavHostController?>(null)

    val navController = _navController.asStateFlow()

    fun initializeNavController(navController: NavHostController) {
        _navController.value = navController
    }

    private val _bottomNavBarTabKey = MutableStateFlow("home")

    val bottomNavBarTab = _bottomNavBarTabKey.asStateFlow()

    fun navToMainScreen(tab: String) {
        _bottomNavBarTabKey.value = tab
        navController.value!!.navigate("main") {
            popUpTo("main") {}
            launchSingleTop = true
        }
    }

    fun updateBottomNavBarTab(tab: String) {
        _bottomNavBarTabKey.value = tab
    }
}

