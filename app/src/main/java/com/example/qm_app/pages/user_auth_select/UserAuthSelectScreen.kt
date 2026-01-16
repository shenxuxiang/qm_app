package com.example.qm_app.pages.user_auth_select

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.R
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner2
import com.example.qm_app.ui.theme.primaryColor

@Composable
fun UserAuthSelectScreen() {
    PageScaffold(title = "认证新身份") { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(3.dp, 14.dp)
                        .background(color = primaryColor, shape = corner2)
                )
                Text(text = "请选择认证身份", fontSize = 14.sp, lineHeight = 14.sp, color = black4)
            }
            IDCardWidget(
                title = "农户",
                subtitle = "需提供身份证进行认证",
                modifier = Modifier.padding(top = 16.dp),
                icon = painterResource(R.drawable.user_auth_1),
                onTap = { Router.navigate(Route.UserAuthFarmScreen.route) }
            )
            IDCardWidget(
                title = "农机手",
                subtitle = "需提供身份证进行认证",
                modifier = Modifier.padding(top = 16.dp),
                icon = painterResource(R.drawable.user_auth_2),
            )
        }
    }
}