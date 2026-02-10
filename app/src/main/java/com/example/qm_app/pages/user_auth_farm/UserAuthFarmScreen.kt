package com.example.qm_app.pages.user_auth_farm

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.qm_app.R
import com.example.qm_app.components.ButtonWidget
import com.example.qm_app.components.ButtonWidgetType
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.modifier.boxShadow
import com.example.qm_app.pages.user_auth_farm.components.InputWidget
import com.example.qm_app.pages.user_auth_farm.components.ModuleTitleWidget
import com.example.qm_app.pages.user_auth_farm.components.PhotoCardWidget
import com.example.qm_app.pages.user_auth_farm.components.TextParagraphWidget
import com.example.qm_app.remember.KeyboardState
import com.example.qm_app.remember.rememberKeyboardState
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
    val userAuthModel = hiltViewModel<UserAuthViewModel>()
    val systemUiController = rememberSystemUiController()
    val uiState by userAuthModel.uiState.collectAsState()
    val keyboardState = rememberKeyboardState()
    val scrollState = rememberScrollState()

    // 提交按钮是否可用
    val submitButtonDisabled by derivedStateOf {
        uiState.url1.isEmpty() ||
                uiState.url2.isEmpty() ||
                uiState.url3.isEmpty() ||
                uiState.userName.isEmpty() ||
                uiState.idNumber.isEmpty() ||
                uiState.validDate.isEmpty()
    }
    // 恢复StatusBar 中 Icons 的颜色
    LaunchedEffect(systemUiController) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true,
        )
    }

    LaunchedEffect(Unit) {
        Router.controller.currentBackStackEntry?.savedStateHandle?.let {
            val bitmap = it["arguments"] as Bitmap?
            val type = it["requestType"] as String?
            println(type)
            if (bitmap != null && type != null) {
                when (type) {
                    "1" -> {
                        userAuthModel.updateUIState { it -> it.copy(bitmap1 = bitmap) }
                        userAuthModel.uploadIDCardBackFace(bitmap)
                    }

                    "2" -> {
                        userAuthModel.updateUIState { it -> it.copy(bitmap2 = bitmap) }
                        userAuthModel.uploadIDCardFrontFace(bitmap)
                    }

                    "3" -> {
                        userAuthModel.updateUIState { it -> it.copy(bitmap3 = bitmap) }
                        userAuthModel.uploadPhotoOfThird(bitmap)
                    }
                }
                it["arguments"] = null
                it["requestType"] = null
            }
        }
    }

    PageScaffold(
        title = "农户认证",
        bottomBar = {
            if (keyboardState == KeyboardState.Closed) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp)
                        .boxShadow(x = 0.dp, y = 2.dp, blur = 6.dp, color = Color(0x22000000))
                        .background(color = white),
                ) {
                    ButtonWidget(
                        text = "提交",
                        onTap = {
                            val params = mapOf(
                                "portraitUrl" to uiState.url2,
                                "handHeldUrl" to uiState.url3,
                                "nationalEmblemUrl" to uiState.url1,
                                "realName" to uiState.userName,
                                "idCardNum" to uiState.idNumber,
                                "validDate" to uiState.validDate,
                            )
                            userAuthModel.submitFarmAuth(params)
                        },
                        disabled = submitButtonDisabled,
                        type = ButtonWidgetType.Primary,
                        modifier = Modifier
                            .padding(horizontal = 46.dp)
                            .fillMaxWidth()
                            .height(36.dp),
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .imePadding()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
                .verticalScroll(scrollState),
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
            PhotoCardWidget(
                title = "国徽面",
                subtitle = "请上传身份证国徽面",
                resourceId = R.drawable.user_auth_3,
                modifier = Modifier.padding(top = 16.dp),
                photo = uiState.bitmap1,
                onTap = {
                    Router.navigate(
                        Route.CameraScreen.route,
                        parameters = mapOf("requestType" to "1")
                    )
                }
            )
            PhotoCardWidget(
                title = "人像面",
                subtitle = "请上传身份证人像面",
                resourceId = R.drawable.user_auth_4,
                modifier = Modifier.padding(top = 12.dp),
                photo = uiState.bitmap2,
                onTap = {
                    Router.navigate(
                        Route.CameraScreen.route,
                        parameters = mapOf("requestType" to "2")
                    )
                }
            )
            PhotoCardWidget(
                title = "手持照片",
                subtitle = "手持身份证正面",
                resourceId = R.drawable.user_auth_5,
                modifier = Modifier.padding(top = 12.dp),
                photo = uiState.bitmap3,
                onTap = {
                    Router.navigate(
                        Route.CameraScreen.route,
                        parameters = mapOf("requestType" to "3")
                    )
                }
            )
            ModuleTitleWidget(title = "确认身份信息", modifier = Modifier.padding(vertical = 16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(146.dp)
                    .clip(corner10)
                    .background(color = white)
            ) {
                InputWidget(
                    label = "个人姓名",
                    value = uiState.userName,
                    onChange = { input -> userAuthModel.updateUIState { it.copy(userName = input) } }
                )
                InputWidget(
                    label = "身份证号",
                    value = uiState.idNumber,
                    keyboardType = KeyboardType.Number,
                    onChange = { input -> userAuthModel.updateUIState { it.copy(idNumber = input) } }
                )
                InputWidget(
                    bordered = false,
                    label = "有效期限",
                    value = uiState.validDate,
                    keyboardType = KeyboardType.Number,
                    onChange = { input -> userAuthModel.updateUIState { it.copy(validDate = input) } }
                )
            }
            Text(
                text = "注意事项：",
                color = black3,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextParagraphWidget()
        }
    }
}