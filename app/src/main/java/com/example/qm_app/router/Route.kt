package com.example.qm_app.router

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.qm_app.components.SwipeBack
import com.example.qm_app.pages.goods.GoodsScreen
import com.example.qm_app.pages.home.HomeScreen
import com.example.qm_app.pages.login.LoginScreen
import com.example.qm_app.pages.signup.SignUpScreen
import com.example.qm_app.pages.user.UserScreen
import kotlin.reflect.KClass

sealed class Route(
    val route: String,
    val deepLinks: List<NavDeepLink> = emptyList(),
    val arguments: List<NamedNavArgument> = emptyList(),
    val content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit),
) {
    companion object {
        val allRoutes: List<Route> by lazy {
            collectSubclasses(Route::class)
        }

        private fun <T : Any> collectSubclasses(parentClass: KClass<T>): List<Route> {
            return parentClass.sealedSubclasses.flatMap { subclass ->
                when {
                    subclass.objectInstance != null -> listOf(subclass.objectInstance as Route)
                    else -> collectSubclasses(subclass)
                }
            }
        }
    }

    /**
     * 主页
     * */
    object HomeScreen : Route(route = "home", content = { HomeScreen() })

    /**
     * 登录页
     * */
    object LoginScreen : Route(
        route = "login",
        content = { LoginScreen() }
    )

    /**
     * 注册页
     * */
    object SignUpScreen : Route(route = "signUp", content = { SwipeBack { SignUpScreen() } })

    /**
     * 用户
     * */
    object UserScreen : Route(
        route = "user?id={id}",
        arguments = listOf(
            navArgument(name = "id") {
                type = NavType.StringType
                nullable = false
            }
        ),
        deepLinks = listOf(
            navDeepLink { uriPattern = "qm_app://qm.com/user?id={id}" }
        ),
        content = {
            SwipeBack { UserScreen(id = it.arguments!!.getString("id", "")) }
        }
    )

    /**
     * 商品
     * */
    object GoodsScreen : Route(route = "goods", content = { SwipeBack { GoodsScreen() } })
}