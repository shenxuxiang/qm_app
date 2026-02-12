package com.example.qm_app.pages.measure_land

//import javax.inject.Inject
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import androidx.lifecycle.ViewModel
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Polyline
import com.amap.api.maps.model.PolylineOptions
import com.example.qm_app.R
import com.example.qm_app.common.TrackingUserForegroundService
import com.example.qm_app.utils.dpToPx
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class Model1 @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    private val _uiState1 = MutableStateFlow(UiState1())
    val uiState = _uiState1.asStateFlow()

    fun updateUIState(block: UiState1.() -> UiState1) {
        _uiState1.update { it.block() }
    }

    private var serviceConnection: ServiceConnection? = null

    @SuppressLint("StaticFieldLeak")
    private var trackingService: TrackingUserForegroundService? = null
    private var polyline: Polyline? = null

    private val polylineWidth = dpToPx(7)
    private val polylineTexture =
        BitmapDescriptorFactory.fromResource(R.drawable.amap_line_texture_green)

    fun addPolygon(location: Location) {
        val point = LatLng(location.latitude, location.longitude)
        uiState.value.aMapView?.let { mapView ->
            if (polyline == null) {
                val polylineOptions = PolylineOptions().apply {
                    add(point)
                    width(polylineWidth)

                    isUseTexture = true
                    customTexture = polylineTexture
                }

                polyline = mapView.aMap.addPolyline(polylineOptions)
            } else {
                val polylineOptions = polyline!!.options
                polylineOptions.add(point)
                polyline!!.remove()
                polyline = mapView.aMap.addPolyline(polylineOptions)
            }
        }
    }

    /* 开始跟踪 */
    fun handleStartTracking() {
        updateUIState { copy(measureState = MeasureState.InProgress) }

        if (serviceConnection == null) {
            serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    trackingService =
                        (service as TrackingUserForegroundService.ServiceBind).instance
                    // 开启定位追踪
                    trackingService!!.startTracking { addPolygon(it) }
                }

                override fun onServiceDisconnected(name: ComponentName?) {}
            }

            TrackingUserForegroundService.bindService(context, serviceConnection!!)
        } else {
            trackingService!!.startTracking { addPolygon(it) }
        }
    }

    /* 暂停 */
    fun handlePauseTracking() {
        updateUIState { copy(measureState = MeasureState.Pause) }
        trackingService?.stopTracking()
    }

    /* 推测测量 */
    fun handleExitMeasure() {
        TrackingUserForegroundService.unbindService()
        polyline?.remove()
        polyline = null
        updateUIState { copy(measureState = MeasureState.Stop) }
    }
}