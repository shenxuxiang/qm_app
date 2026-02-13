package com.example.qm_app.pages.measure_land

import androidx.compose.runtime.Stable
import com.example.qm_app.common.AMapView

enum class MeasureState { InProgress, Pause, Stop }

@Stable
data class UiState1(
    val initialZoom: Float = 18f,
    val aMapView: AMapView? = null,
    val measureState: MeasureState = MeasureState.Stop,
    val area: Float = 0f,
    val perimeter: Float = 0f,
    val isUserDragging: Boolean = false,
)