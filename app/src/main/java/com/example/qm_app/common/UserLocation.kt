package com.example.qm_app.common

import android.location.Location
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption

object UserLocation {
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
}