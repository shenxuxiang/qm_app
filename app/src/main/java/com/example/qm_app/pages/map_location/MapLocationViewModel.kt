package com.example.qm_app.pages.map_location

import android.location.Location
import androidx.lifecycle.ViewModel
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.geocoder.GeocodeResult
import com.amap.api.services.geocoder.GeocodeSearch
import com.amap.api.services.geocoder.RegeocodeQuery
import com.amap.api.services.geocoder.RegeocodeResult
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.poisearch.PoiSearchV2
import com.example.qm_app.common.QmApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class MapLocationViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())

    val uiState = _uiState.asStateFlow()

    fun updateUIState(black: (UiState) -> UiState) {
        _uiState.update {
            black(it)
        }
    }

    // 更新 POI 列表
    fun updatePoiList(point: LatLng? = null) {
        val position = point ?: uiState.value.aMapView!!.aMap.cameraPosition.target

        val query = PoiSearchV2.Query("", "")
        query.pageNum = 1
        query.pageSize = 10

        val poiSearch = PoiSearchV2(QmApplication.context, query)
        poiSearch.setOnPoiSearchListener(object : PoiSearchV2.OnPoiSearchListener {
            override fun onPoiSearched(result: PoiResultV2?, p1: Int) {
                result?.let {
                    // 我们取 poi 中的 latLonPoint、snippet、title、cityName、adName 这些属性
                    updateUIState { uiState -> uiState.copy(poiList = it.pois) }
                }
            }

            override fun onPoiItemSearched(p0: PoiItemV2?, p1: Int) {}
        })

        // 设置检索范围，半径 500M
        poiSearch.bound = PoiSearchV2.SearchBound(
            LatLonPoint(position.latitude, position.longitude),
            500,
        )

        // 开始检索
        poiSearch.searchPOIAsyn()
    }

    suspend fun getLocationFromPoint(point: LatLng): Location {
        return suspendCoroutine { continuation ->
            val geocodeSearch = GeocodeSearch(QmApplication.context)
            geocodeSearch.setOnGeocodeSearchListener(object :
                GeocodeSearch.OnGeocodeSearchListener {
                override fun onRegeocodeSearched(result: RegeocodeResult?, p1: Int) {
                    TODO("Not yet implemented")
                }

                override fun onGeocodeSearched(p0: GeocodeResult?, p1: Int) {}
            })

            val regeocodeQuery = RegeocodeQuery(
                LatLonPoint(point.latitude, point.longitude),
                100f,
                GeocodeSearch.AMAP,
            )
            geocodeSearch.getFromLocationAsyn(regeocodeQuery)
        }
    }
}