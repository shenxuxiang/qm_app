package com.example.qm_app.pages.user_auth_farm

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.qm_app.CameraActivity
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.pages.user_auth_farm.components.ModuleTitleWidget
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black4
import com.example.qm_app.ui.theme.corner10
import com.example.qm_app.ui.theme.white

/**
 * 用户认证-农户认证
 * */
@Composable
fun UserAuthFarmScreen() {
    val context = LocalContext.current
    val hasCameraPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val requestCameraPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { maps ->
            hasCameraPermission.value = maps.all { it.value }
//            Router.navigate(Route.CameraScreen.route)
            CameraActivity.startActivity(context)
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
                    .padding(horizontal = 12.dp)
                    .clickable(onClick = {
                        Router.navigate(Route.CameraScreen.route)
                    }),
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
        }
    }
}