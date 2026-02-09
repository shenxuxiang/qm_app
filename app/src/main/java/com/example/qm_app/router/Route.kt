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
import com.example.qm_app.pages.add_user_address.AddUserAddressScreen
import com.example.qm_app.pages.camera.CameraScreen
import com.example.qm_app.pages.login.LoginScreen
import com.example.qm_app.pages.main.MainScreen
import com.example.qm_app.pages.map_location.MapLocationScreen
import com.example.qm_app.pages.signup.SignUpScreen
import com.example.qm_app.pages.user.UserScreen
import com.example.qm_app.pages.user_address.UserAddressScreen
import com.example.qm_app.pages.user_auth_farm.UserAuthFarmScreen
import com.example.qm_app.pages.user_auth_farm_view.UserAuthFarmViewScreen
import com.example.qm_app.pages.user_auth_select.UserAuthSelectScreen
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
    object MainScreen : Route(route = "home", content = { MainScreen() })

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
     * 用户认证-选择身份
     * */
    object UserAuthSelectScreen :
        Route(route = "user_auth_select", content = { SwipeBack { UserAuthSelectScreen() } })

    /**
     * 用户认证-农户认证
     * */
    object UserAuthFarmScreen :
        Route(route = "user_auth_farm", content = { SwipeBack { UserAuthFarmScreen() } })

    /**
     * 用户认证-查看农户认证
     * */
    object UserAuthFarmViewScreen :
        Route(route = "user_auth_farm_view", content = { SwipeBack { UserAuthFarmViewScreen() } })

    /**
     * 相机界面
     * */
    object CameraScreen : Route(
        route = "camera/{requestType}",
        arguments = listOf(navArgument(name = "requestType") {
            defaultValue = "1"
            type = NavType.StringType
        }),
        content = { backStateEntry ->
            CameraScreen(requestType = backStateEntry.arguments?.getString("requestType") ?: "1")
        })

    /**
     * 高德地图定位界面
     * */
    object MapLocationScreen : Route(
        route = "map_location",
        content = {
            MapLocationScreen()
        }
    )

    /**
     * 地址管理
     * */
    object UserAddressScreen : Route(
        route = "user_address",
        content = {
            SwipeBack { UserAddressScreen() }
        })

    /**
     * 新增地址
     * */
    object AddUserAddressScreen : Route(
        route = "add_user_address",
        content = {
            SwipeBack { AddUserAddressScreen() }
        })
}