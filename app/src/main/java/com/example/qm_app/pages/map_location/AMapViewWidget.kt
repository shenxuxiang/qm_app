package com.example.qm_app.pages.map_location

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.example.qm_app.R
import com.example.qm_app.common.UserLocation
import com.example.qm_app.components.toast.Toast


@Composable
fun AMapViewWidget() {
    val density = LocalDensity.current
    val context = LocalContext.current
    val initialZoom = remember { 18f }
    val lifecycleOwner = LocalLifecycleOwner.current
    val marker = remember { mutableStateOf<Marker?>(null) }

    val aMapView = remember {
        AMapView(context, lifecycleOwner.lifecycle) { view ->
            UserLocation.getCurrentLocation { location ->
                location?.let {
                    val point = LatLng(it.latitude, it.longitude)
                    view.moveCamera(point, initialZoom)
                    marker.value = view.addMarker(
                        point = point,
                        icon = view.customMarkerIcon(
                            R.drawable.map_location_icon,
                            with(density) { 70.dp.toPx().toInt() },
                            with(density) { 70.dp.toPx().toInt() },
                        ),
                    )
                } ?: run {
                    Toast.postShowWarningToast("定位失败")
                }
            }
        }.apply {
            aMap.mapType = AMap.MAP_TYPE_NORMAL
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isScaleControlsEnabled = false
            uiSettings.logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_RIGHT
        }
    }

    AndroidView(
        factory = { aMapView },
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
    )
}