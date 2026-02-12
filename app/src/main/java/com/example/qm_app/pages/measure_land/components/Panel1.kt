package com.example.qm_app.pages.measure_land.components

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.AMap
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.example.qm_app.R
import com.example.qm_app.common.AMapView
import com.example.qm_app.common.UserLocationManager
import com.example.qm_app.pages.measure_land.Model1
import com.example.qm_app.ui.theme.primaryColor

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
const val POST_NOTIFICATIONS = android.Manifest.permission.POST_NOTIFICATIONS

@Composable
fun Panel1() {
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

        val model: Model1 = hiltViewModel()
        val uiState by model.uiState.collectAsState()

        val aMapView = remember {
            AMapView(context, lifecycleOwner.lifecycle) { mapView ->
                mapView.aMap.mapType = AMap.MAP_TYPE_SATELLITE
                model.updateUIState { copy(aMapView = mapView) }
                mapView.aMap.setPointToCenter(mapCenterPoint.x, mapCenterPoint.y)
                UserLocationManager.getCurrentLocation { location ->
                    location?.let {
                        val myLocationStyle = MyLocationStyle().apply {
                            interval(3000)
                            strokeWidth(0f)
                            radiusFillColor(primaryColor.copy(alpha = 0.3f).toArgb())
                            myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)
                        }

                        mapView.aMap.isMyLocationEnabled = true
                        mapView.aMap.myLocationStyle = myLocationStyle
                        mapView.moveCamera(
                            zoom = uiState.initialZoom,
                            latLng = LatLng(location.latitude, location.longitude),
                        )
                    }
                }
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
                    model.handleStartTracking()
                }
            }

        /* 开始追踪轨迹 */
        fun handleStart() {
            if (hasNotificationPermission.value) {
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
            model.handleExitMeasure()
        }


        val onBackPressedDispatcher =
            checkNotNull(LocalOnBackPressedDispatcherOwner.current).onBackPressedDispatcher
        DisposableEffect(Unit) {
            val onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            }
            onBackPressedDispatcher.addCallback(onBackPressedCallback)
            onDispose { onBackPressedCallback.remove() }
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