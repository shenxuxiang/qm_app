package com.example.testactivity

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
    fun getCurrentLocation(block: (location: Location) -> Unit) {
        lateinit var locationListener: LocationListener

        val provider = if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            LocationManager.GPS_PROVIDER
        } else {
            LocationManager.NETWORK_PROVIDER
        }

        val handler = Handler(Looper.getMainLooper())
        val timerTask = Runnable {
            // 取消原来的监听，再绑定新的监听
            locationManager.removeUpdates(locationListener)
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000L,
                0f,
                locationListener
            )
        }

        locationListener = LocationListener { location ->
            // 取消对 requestLocationUpdates 的监听，这里只需要触发一次就行了。
            locationManager.removeUpdates(locationListener)
            // 取消定时任务，避免重复执行
            handler.removeCallbacks(timerTask)
            block(location)
        }

        locationManager.requestLocationUpdates(
            provider,
            1000L,
            0f,
            locationListener
        )

        // 只针对采用 GPS_PROVIDER 模式的定位，因为在有些情况下，如果用户处于静止状态时，GPS 定位可能一致不返回结果。
        if (provider == LocationManager.GPS_PROVIDER) {
            handler.postDelayed(timerTask, 6000)
        }
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
