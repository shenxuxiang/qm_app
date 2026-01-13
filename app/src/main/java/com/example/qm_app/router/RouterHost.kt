package com.example.qm_app.router

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.qm_app.CommonViewModel
import com.example.qm_app.common.QmApplication
import com.example.qm_app.components.SwipeBackEventBus
import kotlinx.coroutines.delay

@Composable
fun RouterHost(startDestination: String) {
    val initialDurationMillis = 400
    val navController = rememberNavController()
    val commonViewModel = hiltViewModel<CommonViewModel>()

    // 初始化 Router
    Router.init(navController)
    QmApplication.commonViewModel = commonViewModel

    val animationSpec = remember {
        mutableStateOf(value = tween<IntOffset>(durationMillis = initialDurationMillis))
    }

    LaunchedEffect(animationSpec.value) {
        if (animationSpec.value.durationMillis == 0) {
            // 延迟 300 ms 后在恢复 animationSpec 的动画时间;
            // 这个延迟时间一定要比滑动返回时的动画时间要大
            delay(400)
            animationSpec.value = tween(durationMillis = initialDurationMillis)
        }
    }

    LaunchedEffect(Unit) {
        SwipeBackEventBus.event.collect {
            animationSpec.value = tween(durationMillis = 0)
        }
    }

    NavHost(
        navController,
        startDestination,
        enterTransition = {
            slideIntoContainer(
                towards = SlideDirection.Start,
                animationSpec = animationSpec.value,
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = SlideDirection.Start,
                animationSpec = animationSpec.value,
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = SlideDirection.End,
                animationSpec = animationSpec.value,
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = SlideDirection.End,
                animationSpec = animationSpec.value,
            )
        },
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