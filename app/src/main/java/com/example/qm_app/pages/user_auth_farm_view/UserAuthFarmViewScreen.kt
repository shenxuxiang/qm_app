package com.example.qm_app.pages.user_auth_farm_view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.qm_app.components.ButtonWidget
import com.example.qm_app.components.ButtonWidgetType
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.modifier.boxShadow
import com.example.qm_app.pages.user_auth_farm.components.ModuleTitleWidget
import com.example.qm_app.pages.user_auth_farm_view.components.FormItem
import com.example.qm_app.pages.user_auth_farm_view.components.PhotoBoxWidget
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.white

@Composable
fun UserAuthFarmViewScreen() {
    val model = hiltViewModel<UserAuthFarmViewViewModel>()
    val uiState by model.uiState.collectAsState()
    val scrollState = rememberScrollState()

    PageScaffold(
        title = "查看农户认证",
        bottomBar = {
            if (uiState.status == 2)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp)
                        .boxShadow(x = 0.dp, y = 2.dp, blur = 6.dp, color = Color(0x22000000))
                        .background(color = white),
                ) {
                    ButtonWidget(
                        text = "重新认证",
                        type = ButtonWidgetType.Primary,
                        onTap = { Router.navigate(Route.UserAuthFarmScreen.route) },
                        modifier = Modifier
                            .padding(horizontal = 46.dp)
                            .fillMaxWidth()
                            .height(36.dp),
                    )
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
            ModuleTitleWidget(
                title = "认证信息",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(146.dp)
                    .clip(corner10)
                    .background(color = white)
            ) {
                FormItem(label = "身份类型", value = uiState.userTypeName, bordered = true)
                FormItem(label = "认证状态", value = uiState.checkStatusName, bordered = true)
                FormItem(label = "审核备注", value = uiState.checkRemark, bordered = false)
            }
            ModuleTitleWidget(title = "认证信息", modifier = Modifier.padding(vertical = 16.dp))
            PhotoBoxWidget(title = "国徽面", url = uiState.nationalEmblemUrl)
            PhotoBoxWidget(
                title = "人像面",
                url = uiState.portraitUrl,
                modifier = Modifier.padding(top = 12.dp)
            )
            PhotoBoxWidget(
                title = "手持照片",
                url = uiState.handHeldUrl,
                modifier = Modifier.padding(top = 12.dp)
            )
            ModuleTitleWidget(title = "身份信息", modifier = Modifier.padding(vertical = 16.dp))
            Column(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth()
                    .height(146.dp)
                    .clip(corner10)
                    .background(color = white)
            ) {
                FormItem(label = "个人姓名", value = uiState.realName, bordered = true)
                FormItem(label = "身份证号", value = uiState.idCardNum, bordered = true)
                FormItem(label = "有效期限", value = uiState.validDate, bordered = false)
            }
        }
    }
}

