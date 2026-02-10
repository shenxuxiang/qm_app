package com.example.qm_app.pages.measure_land.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.R
import com.example.qm_app.pages.measure_land.MeasureState
import com.example.qm_app.pages.measure_land.Model1
import com.example.qm_app.pages.measure_land.UiState1
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black6

@Composable
fun ControllerPanel1(
    model: Model1,
    uiState: UiState1,
    modifier: Modifier,
    onStart: () -> Unit,
    onPause: () -> Unit,
) {
    val width = LocalConfiguration.current.screenWidthDp.dp
    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
        ControllerPanelShape()
        val imageIcon =
            if (uiState.measureState == MeasureState.InProgress) {
                painterResource(R.drawable.measure_land_pause)
            } else {
                painterResource(R.drawable.measure_land_start)
            }

        val stateText = if (uiState.measureState == MeasureState.InProgress) {
            "暂停"
        } else {
            "开始"
        }

        Image(
            painter = imageIcon,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(66.dp)
                .clickable(
                    indication = null,
                    interactionSource = null,
                    onClick = {
                        if (uiState.measureState == MeasureState.InProgress) {
                            onPause()
                        } else {
                            onStart()
                        }
                    }
                )
        )
//        Text(
//            text = stateText,
//            color = black6,
//            fontSize = 13.sp,
//            lineHeight = 13.sp,
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 75.dp)
//        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width((width - 66.dp) / 2)
                .align(Alignment.TopStart),
        ) {
            Text(
                text = "0",
                color = black3,
                fontSize = 22.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 37.dp, bottom = 16.dp)
            )
            Text(
                text = "估算面积（亩）",
                color = black6,
                fontSize = 13.sp,
                lineHeight = 13.sp,
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width((width - 66.dp) / 2)
                .align(Alignment.TopEnd),
        ) {
            Text(
                text = "0",
                color = black3,
                fontSize = 22.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 37.dp, bottom = 16.dp)
            )
            Text(
                text = "目前周长（米）",
                color = black6,
                fontSize = 13.sp,
                lineHeight = 13.sp,
            )
        }
    }
}