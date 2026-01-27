package com.example.qm_app.pages.map_location

import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.AMap
import com.amap.api.maps.MapsInitializer
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.PolygonOptions
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.corner6
import com.example.qm_app.ui.theme.gray
import com.example.qm_app.ui.theme.white


@Composable
fun AMapViewWidget() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember {
        // 在构造 MapView 之前必须进行合规检查
        MapsInitializer.updatePrivacyShow(context, true, true);
        MapsInitializer.updatePrivacyAgree(context, true);
        AMapView(context)
    }

    LaunchedEffect(mapView) {
        mapView.apply {
            bindToLifecycle(lifecycle = lifecycleOwner.lifecycle)
            aMap.mapType = AMap.MAP_TYPE_SATELLITE

            aMap.setInfoWindowAdapter(object : AMap.InfoWindowAdapter {
                override fun getInfoWindow(remark: Marker?): View? {
                    val composeView = ComposeView(context)
                    composeView.setContent {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(white, corner6)
                        ) {
                            Text(
                                text = remark?.title ?: "",
                                color = black3,
                                fontSize = 16.sp,
                                lineHeight = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            Text(
                                text = remark?.snippet ?: "",
                                color = gray,
                                fontSize = 13.sp,
                                lineHeight = 13.sp,
                            )
                        }
                    }

                    composeView.setBackgroundColor(Color.Transparent.toArgb())
                    return composeView
                }

                override fun getInfoContents(remark: Marker?): View? {
                    return null
                }
            })

            aMap.addOnMapClickListener { point ->
                println("onClick: $point")
                val marker = addMarker(point)
            }

            aMap.addOnMarkerDragListener(object : AMap.OnMarkerDragListener {
                override fun onMarkerDragStart(marker: Marker?) {
                    println("onMarkerDragStart: ${marker?.position}")
                }

                override fun onMarkerDrag(marker: Marker?) {
                    println("拖拽中 - 位置: ${marker?.position}")
                }

                override fun onMarkerDragEnd(marker: Marker?) {
                    println("拖拽结束 - 最终位置: ${marker?.position}")
                }
            })
            val points = listOf(
                LatLng(39.999391, 116.135972),
                LatLng(39.898323, 116.057694),
                LatLng(39.900430, 116.265061),
                LatLng(39.955192, 116.140092),
            )

            aMap.addPolygon(PolygonOptions().apply {
                addAll(points)
                strokeWidth(21f)
                strokeColor(Color(0x60FF9900).toArgb())
                fillColor(Color(0x60FF4949).toArgb())
            })
        }
    }

    AndroidView(
        factory = { mapView },
        update = { view -> println("======================123") },
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
    )
}