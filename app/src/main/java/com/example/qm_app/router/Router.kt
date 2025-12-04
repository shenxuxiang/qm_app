package com.example.qm_app.router

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qm_app.CommonViewModel
import com.example.qm_app.common.QmApplication

@Composable
fun Router(startDestination: String) {
    val navController = rememberNavController()
    val commonViewModel = hiltViewModel<CommonViewModel>()
    val animationSpec = tween<IntOffset>(durationMillis = 300)

    QmApplication.navController = navController
    QmApplication.commonViewModel = commonViewModel

    NavHost(
        navController,
        startDestination,
        enterTransition = { slideIntoContainer(towards = SlideDirection.Start, animationSpec) },
        exitTransition = { slideOutOfContainer(towards = SlideDirection.Start, animationSpec) },
        popEnterTransition = { slideIntoContainer(towards = SlideDirection.End, animationSpec) },
        popExitTransition = { slideOutOfContainer(towards = SlideDirection.End, animationSpec) },
    ) {
        repeat(Route.allRoutes.size) { index ->
            val route = Route.allRoutes[index]
            composable(
                route = route.route,
                content = route.content,
                arguments = route.arguments,
                deepLinks = route.deepLinks,
            )
        }
    }
}