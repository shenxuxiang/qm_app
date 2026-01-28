package com.example.qm_app.pages.map_location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle

@SuppressLint("ViewConstructor")
class AMapView(private val context: Context) : MapView(context) {
    val aMap: AMap get() = map
    val uiSettings: UiSettings get() = aMap.uiSettings// 实例化 UiSettings 类对象

    fun moveCamera(latLng: LatLng, zoom: Float) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        aMap.animateCamera(cameraUpdate)
    }

    fun setControls() {
        uiSettings.isZoomControlsEnabled = false
        uiSettings.isCompassEnabled = false
        uiSettings.isScaleControlsEnabled = false
        uiSettings.logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_RIGHT
    }

    fun addMarker(point: LatLng): Marker {
        val markerOptions = MarkerOptions().apply {
            position(point)
            title("柏庄香府")
            snippet("柏庄香府: ${point.latitude}, ${point.longitude}")
            draggable(true)
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)) // 可选：设置颜色
        }

        return aMap.addMarker(markerOptions)
    }

    fun setMyLocationStyle() {
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.showMyLocation(true)
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        aMap.myLocationStyle = myLocationStyle
        aMap.isMyLocationEnabled = true
    }

    fun addOnMyLocationChangeListener(block: (Location) -> Unit): () -> Unit {
        aMap.addOnMyLocationChangeListener(block)
        return fun() {
            aMap.removeOnMyLocationChangeListener(block)
        }
    }

    fun bindToLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                onCreate(null)
            }

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                onPause()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                onDestroy()
            }
        })
    }
}
