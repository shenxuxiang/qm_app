package com.example.qm_app.pages.tab.mine.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.ButtonWidget
import com.example.qm_app.components.ButtonWidgetType
import com.example.qm_app.components.QmIcon
import com.example.qm_app.pages.tab.mine.TabMineViewModel
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.gray
import com.example.qm_app.ui.theme.primaryColor
import com.example.qm_app.ui.theme.white

/**
 * 用户认证状态
 * */
@Composable
fun UserCheckStatusWidget(viewModel: TabMineViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val statusName by derivedStateOf {
        when (uiState.userCheckStatus?.checkStatus) {
            0 -> "未认证"
            1 -> "待认证审核"
            2 -> "认证不通过"
            3 -> "${uiState.userCheckStatus!!.userTypeName} | 已认证"
            else -> ""
        }
    }

    val buttonText by derivedStateOf {
        when (uiState.userCheckStatus?.checkStatus) {
            0 -> "立即认证"
            1, 2, 3 -> "查看认证"
            else -> "查看认证"
        }
    }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .background(color = white, shape = corner10)
            .padding(vertical = 10.dp, horizontal = 12.dp),
    ) {
        QmIcon(
            size = 22.dp,
            icon = QmIcons.Stroke.Confirm,
            tint = if (uiState.userCheckStatus?.checkStatus == 3) primaryColor else gray,
        )
        Text(
            text = statusName,
            color = black3,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(start = 5.dp)
        )
        Box(modifier = Modifier.weight(1f))
        ButtonWidget(
            ghost = true,
            text = buttonText,
            type = ButtonWidgetType.Primary,
            modifier = Modifier.size(80.dp, 28.dp),
            onTap = {
                Router.navigate(Route.UserAuthSelectScreen.route)
                if (uiState.userCheckStatus?.checkStatus == 0) {
                    //Router.navigate(Route.UserAuthSelectScreen.route)
                }
            },
        )
    }
}