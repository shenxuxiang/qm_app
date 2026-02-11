package com.example.qm_app.common

import android.location.Location
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption

object UserLocationManager {
    /**
     * 用于一次性定位
     * */
    fun getCurrentLocation(black: (Location?) -> Unit) {
        val aMapLocationClient = AMapLocationClient(QmApplication.context)
        aMapLocationClient.setLocationListener { location ->
            if (location.errorCode == 0) {
                black(location)
            } else {
                black(null)
            }
            aMapLocationClient.stopLocation()
            aMapLocationClient.onDestroy()
        }

        val option = AMapLocationClientOption().apply {
            isOnceLocation = true
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        }
        aMapLocationClient.setLocationOption(option)
        aMapLocationClient.startLocation()
    }

    /**
     * 用于持续性定位
     * @return () -> Unit 取消持续定位
     * */
    fun requestLocationUpdates(black: (Location) -> Unit): () -> Unit {
        val aMapLocationClient = AMapLocationClient(QmApplication.context)
        aMapLocationClient.setLocationListener { location ->
            if (location.errorCode == 0) black(location)
        }

        val option = AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            interval = 1000
            isWifiScan = true
        }
        aMapLocationClient.setLocationOption(option)
        aMapLocationClient.startLocation()
        fun cancel() {
            aMapLocationClient.stopLocation()
            aMapLocationClient.onDestroy()
        }
        return ::cancel
    }
}