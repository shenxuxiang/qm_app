package com.example.qm_app.pages.map_location.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions


@Composable
fun AMapViewWidget(onMapReady: (AMapView) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val aMapView = remember {
        AMapView(context, lifecycleOwner.lifecycle, onMounted = onMapReady).apply {
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