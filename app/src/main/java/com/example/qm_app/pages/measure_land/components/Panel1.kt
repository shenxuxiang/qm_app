package com.example.qm_app.pages.measure_land.components

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.example.qm_app.R
import com.example.qm_app.common.AMapView
import com.example.qm_app.common.UserLocationManager
import com.example.qm_app.components.Alert
import com.example.qm_app.pages.measure_land.Model1
import com.example.qm_app.ui.theme.primaryColor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val POST_NOTIFICATIONS = android.Manifest.permission.POST_NOTIFICATIONS

@Composable
fun Panel1(event: Channel<Boolean>, model: Model1) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart,
    ) {
        val view = LocalView.current
        val density = LocalDensity.current
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        val topBarHeight = dimensionResource(R.dimen.top_bar_height)
        val statusBarsPadding = WindowInsets.statusBars.asPaddingValues()
        val mapCenterPoint = remember {
            IntOffset(
                x = view.width / 2,
                y = with(density) {
                    (view.height -
                            statusBarsPadding.calculateTopPadding().toPx() -
                            topBarHeight.toPx() - 175.dp.toPx()
                            ).toInt() / 2
                }
            )
        }

        val uiState by model.uiState.collectAsState()

        val coroutineScope = rememberCoroutineScope()

        val aMapView = remember {
            AMapView(context, lifecycleOwner.lifecycle) { mapView ->
                mapView.aMap.mapType = AMap.MAP_TYPE_SATELLITE
                model.updateUIState { copy(aMapView = mapView) }
                mapView.aMap.setPointToCenter(mapCenterPoint.x, mapCenterPoint.y)
                UserLocationManager.getCurrentLocation { location ->
                    location?.let {
                        val myLocationStyle = MyLocationStyle().apply {
                            interval(1000)
                            strokeWidth(0f)
                            radiusFillColor(primaryColor.copy(alpha = 0.3f).toArgb())
                            myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER)
                        }

                        mapView.aMap.isMyLocationEnabled = true
                        mapView.aMap.myLocationStyle = myLocationStyle
                        mapView.moveCamera(
                            zoom = uiState.initialZoom,
                            latLng = LatLng(location.latitude, location.longitude),
                        )
                    }
                }
                /* 添加用户拖拽监听 */
                var job: Job? = null
                mapView.setAMapDragGesture(
                    onDrag = {
                        if (!uiState.isUserDragging) {
                            job?.cancel()
                            model.updateUIState { copy(isUserDragging = true) }
                        }
                    },
                    onDragEnd = {
                        if (uiState.isUserDragging) {
                            job = Job()
                            coroutineScope.launch(job) {
                                delay(5000)
                                model.updateUIState { copy(isUserDragging = false) }
                            }
                        }
                    },
                )
            }
        }

        /* 获取通知权限 */
        val hasNotificationPermission = remember {
            mutableStateOf(
                value =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        ContextCompat.checkSelfPermission(
                            context,
                            POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    } else {
                        true
                    }
            )
        }

        /* 请求通知权限 */
        val requestPermission =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                hasNotificationPermission.value = it
                if (it) {
                    // 设置父容器 canPop 为 false
                    event.trySend(false)
                    model.handleStartTracking()
                }
            }

        /* 开始追踪轨迹 */
        fun handleStart() {
            if (hasNotificationPermission.value) {
                // 设置父容器 canPop 为 false
                event.trySend(false)
                model.handleStartTracking()
            } else {
                @SuppressLint("ALL")
                requestPermission.launch(POST_NOTIFICATIONS)
            }
        }

        /* 暂停追踪轨迹 */
        fun handlePause() {
            model.handlePauseTracking()
        }

        /* 退出测量 */
        fun handleExitMeasure() {
            Alert.confirm(
                view = view,
                text = "确定要退出测量吗？",
                onConfirm = {
                    // 设置父容器 canPop 为 true
                    event.trySend(true)
                    model.handleExitMeasure()
                },
            )
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { aMapView }
        )

        ControllerPanel1(
            uiState = uiState,
            onStart = ::handleStart,
            onPause = ::handlePause,
            onExit = ::handleExitMeasure,
            modifier = Modifier
                .fillMaxWidth()
                .height(155.dp)
                .align(Alignment.BottomStart),
        )
    }
}