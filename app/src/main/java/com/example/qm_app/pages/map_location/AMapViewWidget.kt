package com.example.qm_app.pages.map_location

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapException
import com.amap.api.maps.MapsInitializer
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.example.qm_app.components.ButtonWidget
import com.example.qm_app.components.ButtonWidgetType


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
            setMyLocationStyle()


        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AndroidView(
            factory = { mapView },
            update = { view -> println("======================123") },
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
        )
        ButtonWidget(
            text = "搜索",
            type = ButtonWidgetType.Primary,
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp),
            onTap = {
                try {
                    val query = PoiSearch.Query("公园", "", "芜湖市")
                    query.pageSize = 10
                    query.pageNum = 1

                    val poiSearch = PoiSearch(context, query)
                    poiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
                        override fun onPoiSearched(
                            p0: PoiResult?,
                            p1: Int
                        ) {
                            println("p1: ${p0?.pois}")
                            println("p1: $p1")
                        }

                        override fun onPoiItemSearched(
                            p0: PoiItem?,
                            p1: Int
                        ) {
                            println("p1: ${p0?.latLonPoint}")
                            println("p1: ${p0?.title}")
                            println("p1: ${p0?.snippet}")
                            println("p1: ${p0?.direction}")
                            println("p1: ${p0}")
                        }
                    })

                    poiSearch.searchPOIAsyn();
                } catch (e: AMapException) {
                    println(e.message)
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        )
    }

}