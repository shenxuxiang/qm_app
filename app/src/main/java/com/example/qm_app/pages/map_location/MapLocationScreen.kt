package com.example.qm_app.pages.map_location

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

private const val ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION
private const val ACCESS_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION

@Composable
fun MapLocationScreen() {
    val context = LocalContext.current
    val hasLocationPermission = remember {
        val p1 = ContextCompat.checkSelfPermission(
            context,
            ACCESS_FINE_LOCATION
        )
        val p2 = ContextCompat.checkSelfPermission(
            context,
            ACCESS_COARSE_LOCATION
        )

        mutableStateOf(
            p1 == PackageManager.PERMISSION_GRANTED ||
                    p2 == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            hasLocationPermission.value = results.any { it.value }
        }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission.value) {
            requestPermission.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
        }
    }

    if (hasLocationPermission.value) AMapViewWidget()
}
