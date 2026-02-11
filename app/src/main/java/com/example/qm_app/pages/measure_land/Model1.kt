package com.example.qm_app.pages.measure_land

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import androidx.lifecycle.ViewModel
import com.amap.api.maps.model.PolylineOptions
import com.example.qm_app.common.TrackingUserForegroundService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class Model1 @Inject constructor() : ViewModel() {
    private val _uiState1 = MutableStateFlow(UiState1())
    val uiState = _uiState1.asStateFlow()

    fun updateUIState(block: (UiState1) -> UiState1) {
        _uiState1.update { block(it) }
    }

    private var serviceConnection: ServiceConnection? = null
    private var trackingService: TrackingUserForegroundService? = null

    fun updatePolygon(location: Location) {
        uiState.value.aMapView?.let { mapView ->
            val polylineOptions = PolylineOptions().apply {
                
            }

            mapView.aMap.addPolyline(polylineOptions)
        }
    }

    fun handleStartTracking(context: Context) {
        if (serviceConnection == null) {
            serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    trackingService =
                        (service as TrackingUserForegroundService.ServiceBind).instance

                    trackingService!!.startTracking { location ->
                        println("location: ${location.latitude}, ${location.longitude}")
                    }
                }

                override fun onServiceDisconnected(name: ComponentName?) {}
            }

            TrackingUserForegroundService.bindService(context, serviceConnection!!)
        } else {
            trackingService!!.startTracking { location ->
                println("location: ${location.latitude}, ${location.longitude}")
            }
        }
    }

    fun handleStopTracking() {
        trackingService?.stopTracking()
    }
}