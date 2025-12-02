package com.example.qm_app.common

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.net.toUri

object QmExternalNavigationManager {
    class ExternalNavPackage(val label: String, val uri: Uri, val packageName: String) {}

    private const val GAODE = "androidamap://route"
    private const val TENGXUN = "qqmap://map/routeplan"
    private const val BAIDU = "baidumap://map/direction"

    private val externalNavPackages = listOf(
        ExternalNavPackage(
            label = "腾讯地图",
            uri = TENGXUN.toUri(),
            packageName = "com.tencent.map"
        ),
        ExternalNavPackage(
            label = "百度地图",
            uri = BAIDU.toUri(),
            packageName = "com.baidu.BaiduMap"
        ),
        ExternalNavPackage(
            label = "高德地图",
            uri = GAODE.toUri(),
            packageName = "com.autonavi.minimap"
        ),
    )

    private fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(
                packageName,
                PackageManager.MATCH_ALL
            )
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun queryNavList(context: Context): ArrayList<ExternalNavPackage> {
        val result = ArrayList<ExternalNavPackage>()
        for (item in externalNavPackages) {
            if (isAppInstalled(context, item.packageName)) result.add(item)
        }

        return result
    }


    fun startActivity(
        context: Context,
        packageName: String,
        destination: String,
        lat: String,
        lng: String,
    ): Boolean {
        val nav =
            externalNavPackages.find { item -> item.packageName == packageName }
        var uri: Uri? = null
        when (nav?.label) {
            "百度地图" -> {
                uri = Uri.parse("${nav.uri.toString()}?destination=$destination&mode=driving")
            }

            "高德地图" -> {
                uri =
                    Uri.parse("${nav.uri.toString()}?sourceApplication=$packageName&dname=$destination&dlat=$lat&dlon=$lng&dev=1&t=0")
            }

            "腾讯地图" -> {
                uri =
                    Uri.parse("${nav.uri.toString()}?type=drive&to=$destination&tocoord=$lat,$lng&coordtype=wgs84")
            }

            else -> {}
        }

        return uri?.let {
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
            true
        } ?: run {
            false
        }
    }
}