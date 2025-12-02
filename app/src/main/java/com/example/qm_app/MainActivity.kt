package com.example.qm_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.qm_app.common.QmApplication
import com.example.qm_app.pages.goods.GoodsScreen
import com.example.qm_app.pages.main.MainScreen
import com.example.qm_app.pages.user.UserScreen
import com.example.qm_app.ui.theme.Qm_appTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = Color(0xFF000000).toArgb()

        setContent {
            Qm_appTheme {
                val navController = rememberNavController()
                val commonViewModel = hiltViewModel<CommonViewModel>()

                QmApplication.commonViewModel = commonViewModel
                commonViewModel.initializeNavController(navController)

                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    NavHost(startDestination = "main", navController = navController) {
                        composable(route = "main") {
                            MainScreen()
                        }
                        composable(route = "goods") {
                            GoodsScreen()
                        }
                        composable(
                            route = "user?id={id}",
                            arguments = listOf(
                                navArgument(name = "id") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            ),
                            deepLinks = listOf(
                                navDeepLink { uriPattern = "qm_app://qm.com/user?id={id}" }
                            )
                        ) { backStackEntry ->
                            UserScreen(
                                id = backStackEntry.arguments!!.getString("id", "")
                            )
                        }
                    }
                }
            }
        }
    }
}
