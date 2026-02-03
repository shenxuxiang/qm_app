package com.example.qm_app.pages.map_location

import androidx.compose.runtime.Stable
import com.amap.api.location.AMapLocation
import com.amap.api.maps.model.Marker
import com.amap.api.services.core.PoiItemV2
import com.example.qm_app.pages.map_location.components.AMapView

@Stable
data class UiState(
    val marker: Marker? = null,
    val initialZoom: Float = 18f,
    val aMapView: AMapView? = null,
    val userLocation: AMapLocation? = null,
    val poiList: List<PoiItemV2> = emptyList() // POI 集合
)