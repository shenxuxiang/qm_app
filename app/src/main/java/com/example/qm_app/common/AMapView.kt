package com.example.qm_app.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.AMapGestureListener
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions

@SuppressLint("ViewConstructor")
class AMapView(private val context: Context, lifecycle: Lifecycle, onMounted: (AMapView) -> Unit) :
    MapView(context) {
    companion object {
        private val DEFAULT_MARKER_ICON = BitmapDescriptorFactory.defaultMarker(
            BitmapDescriptorFactory.HUE_RED
        )
    }

    /**
     * 获取到地图实例
     * */
    val aMap: AMap get() = map

    /**
     * 实例化 UiSettings 类对象
     * */
    val uiSettings: UiSettings get() = aMap.uiSettings

    /**
     * 地图交互
     * */
    fun moveCamera(latLng: LatLng, zoom: Float) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        aMap.animateCamera(cameraUpdate)
    }

    /**
     * 自定义 Icon
     * */
    fun customIcon(@DrawableRes resourceId: Int, width: Int, height: Int): BitmapDescriptor {
        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        bitmap.recycle()
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    /**
     * 添加 Marker
     * */
    fun addMarker(
        point: LatLng,
        title: String? = null,
        snippet: String? = null,
        draggable: Boolean = true,
        icon: BitmapDescriptor = DEFAULT_MARKER_ICON,
    ): Marker {
        val markerOptions = MarkerOptions().apply {
            this.icon(icon)
            this.position(point)
            this.draggable(draggable)

            anchor(0.5f, 0.9f)
            if (title != null) this.title(title)
            if (snippet != null) this.snippet(snippet)
        }

        return aMap.addMarker(markerOptions)
    }

    private fun bindToLifecycle(lifecycle: Lifecycle, onMounted: (AMapView) -> Unit) {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                onCreate(null)
            }

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                onResume()
                onMounted(this@AMapView)
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

    fun setAMapDragGesture(onDrag: () -> Unit = {}, onDragEnd: () -> Unit = {}) {
        var isDragging = false
        aMap.setAMapGestureListener(object : AMapGestureListener {
            override fun onDoubleTap(p0: Float, p1: Float) {}
            override fun onSingleTap(p0: Float, p1: Float) {}
            override fun onFling(p0: Float, p1: Float) {}
            override fun onScroll(p0: Float, p1: Float) {
                isDragging = true
                onDrag()
            }

            override fun onLongPress(p0: Float, p1: Float) {}
            override fun onDown(p0: Float, p1: Float) {}
            override fun onMapStable() {}
            override fun onUp(p0: Float, p1: Float) {
                if (isDragging) {
                    isDragging = false
                    onDragEnd()
                }
            }
        })
    }

    init {
        bindToLifecycle(lifecycle, onMounted)
    }
}