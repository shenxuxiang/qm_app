package com.example.qm_app.remember

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

private const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
private const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
private const val ACCESS_BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION

class CompositionLocationPermission(permission: Boolean) {

    private val _permission = mutableStateOf(permission)
    val hasPermission: Boolean
        get() = _permission.value

    companion object {
        val Saver: Saver<CompositionLocationPermission, Boolean> = Saver(
            save = { it.hasPermission },
            restore = { CompositionLocationPermission(it) }
        )

        fun checkPermission(context: Context): Boolean {
            val p1 = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION)
            val p2 = ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION)
            val p3 = ContextCompat.checkSelfPermission(context, ACCESS_BACKGROUND_LOCATION)
            return p3 == PackageManager.PERMISSION_GRANTED && (
                    p1 == PackageManager.PERMISSION_GRANTED ||
                            p2 == PackageManager.PERMISSION_GRANTED
                    )
        }
    }

    /**
     * 刷新位置权限
     * @return 返回当前的最新的权限
     * */
    fun refresh(context: Context): Boolean {
        _permission.value = checkPermission(context)
        return _permission.value
    }
}


@Composable
fun rememberCompositionLocationPermission(): CompositionLocationPermission {
    val context = LocalContext.current
    val locationPermission = rememberSaveable(saver = CompositionLocationPermission.Saver) {
        val permission = CompositionLocationPermission.checkPermission(context)
        CompositionLocationPermission(permission)
    }

    // 获取一次最新的位置信息权限
    LaunchedEffect(Unit) {
        locationPermission.refresh(context)
    }

    return locationPermission
}
