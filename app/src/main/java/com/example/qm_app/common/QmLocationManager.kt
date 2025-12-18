package com.example.qm_app.common

import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresPermission

typealias StopUpdateLocation = () -> Unit

class QmLocationManager(private val context: Context) {
    companion object {
        const val ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION

        const val ACCESS_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION
    }

    private var locationManager: LocationManager =
        context.getSystemService(LOCATION_SERVICE) as LocationManager

    /**
     * 获取当前用户位置
     * */
    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
    fun getCurrentLocation(
        block: (location: Location) -> Unit,
        onFailure: ((t: Exception) -> Unit)?,
    ) {
        var locationListenerGPS: LocationListener? = null
        var locationListenerNETWORK: LocationListener? = null

        val handler = Handler(Looper.getMainLooper())
        val timeoutTask = Runnable {
            // 取消原来的监听，再绑定新的监听
            locationManager.removeUpdates(locationListenerGPS!!)
            locationManager.removeUpdates(locationListenerNETWORK!!)

            onFailure?.let {
                it(RuntimeException("Get Location Timeout"))
            }
        }

        locationListenerGPS = LocationListener { location ->
            // 取消对 requestLocationUpdates 的监听，这里只需要触发一次就行了。
            locationManager.removeUpdates(locationListenerGPS!!)
            locationManager.removeUpdates(locationListenerNETWORK!!)
            // 取消定时任务，避免重复执行
            handler.removeCallbacks(timeoutTask)
            block(location)
        }

        locationListenerNETWORK = LocationListener { location ->
            // 取消对 requestLocationUpdates 的监听，这里只需要触发一次就行了。
            locationManager.removeUpdates(locationListenerGPS!!)
            locationManager.removeUpdates(locationListenerNETWORK!!)
            // 取消定时任务，避免重复执行
            handler.removeCallbacks(timeoutTask)
            block(location)
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f,
            locationListenerGPS
        )
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000L,
            0f,
            locationListenerNETWORK
        )

        handler.postDelayed(timeoutTask, 6000)
    }

    /**
     * 实时获取用户位置（1秒中更新一次）
     * */
    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
    fun updateLocation(block: (location: Location) -> Unit): StopUpdateLocation {
        val provider = if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LocationManager.GPS_PROVIDER
        } else {
            LocationManager.NETWORK_PROVIDER
        }

        val locationListener = LocationListener { location ->
            block(location)
        }

        locationManager.requestLocationUpdates(
            provider,
            1000L,
            1f,
            locationListener
        )

        return fun() {
            locationManager.removeUpdates(locationListener)
        }
    }
}
