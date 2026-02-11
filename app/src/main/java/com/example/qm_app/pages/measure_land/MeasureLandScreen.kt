package com.example.qm_app.pages.measure_land

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.example.qm_app.components.Alert
import com.example.qm_app.components.ButtonGroupOption
import com.example.qm_app.components.ButtonGroupWidget
import com.example.qm_app.components.PageScaffold
import com.example.qm_app.pages.measure_land.components.Panel1
import com.example.qm_app.remember.rememberCompositionLocationPermission
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.corner6
import com.example.qm_app.ui.theme.white
import kotlinx.coroutines.launch

const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
const val ACCESS_BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION

private val BUTTON_GROUP_OPTIONS = listOf(
    ButtonGroupOption(label = "绕地模式", value = "0"),
    ButtonGroupOption(label = "定点模式", value = "1"),
    ButtonGroupOption(label = "点选模式", value = "2"),
)

@Composable
fun MeasureLandScreen() {
    val view = LocalView.current
    val context = LocalContext.current
    val compositionLocationPermission = rememberCompositionLocationPermission()

    // 打开系统设置 -->> 修改位置权限
    val openSettings =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // 无论是 RESULT_OK/RESULT_CANCELED，都刷新一次
            compositionLocationPermission.refresh(context)
        }

    // 请求位置信息权限
    val requestPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            // 刷新权限
            val hasPermission = compositionLocationPermission.refresh(context)

            if (!hasPermission) {
                Alert.confirm(
                    view = view,
                    text = "请在权限设置中，添加位置信息权限，并勾选始终允许",
                    onCancel = { Router.popBackStack() },
                    onConfirm = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }

                        openSettings.launch(intent)
                    },
                )
            }
        }

    // 当用户没有权限时，请求权限
    LaunchedEffect(Unit) {
        if (!compositionLocationPermission.hasPermission) {
            requestPermission.launch(
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, ACCESS_BACKGROUND_LOCATION)
            )
        }
    }
    val pagerState = rememberPagerState(initialPage = 0) { 3 }
    val buttonKey = rememberSaveable { mutableStateOf("0") }
    val coroutineScope = rememberCoroutineScope()

    PageScaffold(title = "测量宝") { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fill,
                userScrollEnabled = false,
                beyondViewportPageCount = 1,
            ) {
                when (it) {
                    0 -> Panel1()
                    1 -> Text("定点模式")
                    2 -> Text("点选模式")
                }
            }
            /* 顶部选项卡 */
            ButtonGroupWidget(
                buttonKey = buttonKey.value,
                options = BUTTON_GROUP_OPTIONS,
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 12.dp)
                    .fillMaxWidth()
                    .height(28.dp)
                    .clip(corner6)
                    .background(white)
            ) {
                coroutineScope.launch {
                    buttonKey.value = BUTTON_GROUP_OPTIONS[it].value
                    pagerState.scrollToPage(it)
                }
            }
        }
    }
}