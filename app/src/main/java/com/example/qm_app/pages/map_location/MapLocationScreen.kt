package com.example.qm_app.pages.map_location

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.amap.api.maps.model.LatLng
import com.example.qm_app.R
import com.example.qm_app.common.QmIcons
import com.example.qm_app.common.UserLocation
import com.example.qm_app.components.toast.Toast
import com.example.qm_app.pages.map_location.components.AMapView
import com.example.qm_app.pages.map_location.components.AMapViewWidget
import com.example.qm_app.pages.map_location.components.CircleButton
import com.example.qm_app.pages.map_location.components.DisplayPoiListWidget
import com.example.qm_app.pages.map_location.components.SearchBox
import com.example.qm_app.router.Router

private const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
private const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION

@Composable
fun MapLocationScreen() {
    val context = LocalContext.current
    val hasLocationPermission = remember {
        val p1 = ContextCompat.checkSelfPermission(
            context,
            ACCESS_FINE_LOCATION
        )
        val p2 = ContextCompat.checkSelfPermission(
            context,
            ACCESS_COARSE_LOCATION
        )

        mutableStateOf(
            p1 == PackageManager.PERMISSION_GRANTED ||
                    p2 == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            hasLocationPermission.value = results.any { it.value }
        }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission.value) {
            requestPermission.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
        }
    }

    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val initialZoom = remember { 18f }
    val density = LocalDensity.current
    val view = LocalView.current

    val viewModel = hiltViewModel<MapLocationViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    fun onMapReady(view: AMapView) {
        viewModel.updateUIState { it.copy(aMapView = view) }

        // 设置地图的中心点在屏幕上的具体位置
        // statusBarTop-顶部状态栏的高度，40dp-SearchBox的高度，130dp-中间镂空的高度的一半
        view.aMap.setPointToCenter(
            view.width / 2,
            with(density) { (statusBarTop + 40.dp + 130.dp).toPx().toInt() },
        )
        view.uiSettings.isGestureScaleByMapCenter = true
        view.setAMapDragGesture(onDragEnd = {
            viewModel.updatePoiList()
        })

        UserLocation.getCurrentLocation { location ->
            location?.let {
                val point = LatLng(it.latitude, it.longitude)
                view.moveCamera(point, initialZoom)
                viewModel.updatePoiList(point)
            } ?: run {
                Toast.postShowWarningToast("定位失败")
            }
        }
    }

    fun handleResetUserLocation() {
        UserLocation.getCurrentLocation { location ->
            location?.let {
                val point = LatLng(it.latitude, it.longitude)
                uiState.aMapView?.aMap?.clear()
                uiState.aMapView?.moveCamera(point, initialZoom)
                viewModel.updatePoiList(point)
            } ?: run {
                Toast.postShowWarningToast("定位失败")
            }
        }
    }

    Box(modifier = Modifier
        .navigationBarsPadding()
        .fillMaxSize()) {
        if (hasLocationPermission.value) AMapViewWidget(onMapReady = ::onMapReady)
        SearchBox(placeholder = "搜索位置信息", value = "", onTap = {})

        Box(modifier = Modifier.align(Alignment.TopEnd))

        // 返回上一页
        CircleButton(
            icon = QmIcons.Stroke.Backup,
            onTap = { Router.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = statusBarTop + 60.dp),
        )

        // 重新定位
        CircleButton(
            icon = QmIcons.Stroke.Position,
            onTap = ::handleResetUserLocation,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 12.dp, top = statusBarTop + 60.dp),
        )

        // 地图中心点得 Marker
        Image(
            painter = painterResource(R.drawable.map_location_icon),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = statusBarTop + 125.dp)
                .size(90.dp)
        )

        // 展示周边 POI 检索结果
        DisplayPoiListWidget(
            modifier = Modifier
                .padding(top = statusBarTop + 40.dp + 260.dp)
                .fillMaxSize(),
            uiState = uiState,
            viewModel = viewModel,
        )
    }
}

