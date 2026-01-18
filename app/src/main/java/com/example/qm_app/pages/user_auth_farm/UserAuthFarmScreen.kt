package com.example.qm_app.pages.user_auth_farm

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.R
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.pages.user_auth_farm.components.ModuleTitleWidget
import com.example.qm_app.pages.user_auth_farm.components.PhotoCard
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.white
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * 用户认证-农户认证
 * */
@Composable
fun UserAuthFarmScreen() {
    val context = LocalContext.current
    val bitmap = remember {
        val args =
            Router.controller.currentBackStackEntry?.savedStateHandle?.get<Bitmap>("arguments")
        // 每次获取后都应该清除
        Router.controller.currentBackStackEntry?.savedStateHandle?.set("arguments", null)
        mutableStateOf(args)
    }

    val systemUiController = rememberSystemUiController()

    // 恢复StatusBar 中 Icons 的颜色
    LaunchedEffect(systemUiController) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true,
        )
    }

    PageScaffold(title = "农户认证") { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            ModuleTitleWidget(title = "认证信息")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(color = white, shape = corner10)
                    .padding(horizontal = 12.dp),
            ) {
                Text(
                    text = "身份类型",
                    color = black3,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "农户",
                    color = black4,
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
            ModuleTitleWidget(title = "证件照片上传")
            PhotoCard(
                title = "国徽面",
                subtitle = "请上传身份证国徽面",
                resourceId = R.drawable.user_auth_3,
                modifier = Modifier.padding(top = 16.dp),
                photo = bitmap.value,
                onTap = {
                    Router.navigate(Route.CameraScreen.route)
                }
            )
            PhotoCard(
                title = "人像面",
                subtitle = "请上传身份证人像面",
                resourceId = R.drawable.user_auth_4,
                modifier = Modifier.padding(top = 12.dp),
                photo = bitmap.value,
                onTap = {}
            )
            PhotoCard(
                title = "手持照片",
                subtitle = "手持身份证正面",
                resourceId = R.drawable.user_auth_5,
                modifier = Modifier.padding(top = 12.dp),
                photo = bitmap.value,
                onTap = {}
            )
        }
    }
}