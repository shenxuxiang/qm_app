package com.example.qm_app.pages.measure_land.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.qm_app.R
import com.example.qm_app.components.ButtonWidget
import com.example.qm_app.components.ButtonWidgetType
import com.example.qm_app.pages.measure_land.MeasureState
import com.example.qm_app.pages.measure_land.UiState1
import com.example.qm_app.ui.theme.black3
import com.example.qm_app.ui.theme.black6

@Composable
fun ControllerPanel1(
    uiState: UiState1,
    modifier: Modifier,
    onStart: () -> Unit,
    onPause: () -> Unit,
) {
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
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 37.dp)
                .fillMaxWidth(),
        ) {
            /* 面积 */
            Text(
                text = "${uiState.area}",
                color = black3,
                fontSize = 22.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(66.dp))
            /* 周长 */
            Text(
                text = "${uiState.perimeter}",
                color = black3,
                fontSize = 22.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 75.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "估算面积（亩）",
                color = black6,
                fontSize = 13.sp,
                lineHeight = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stateText,
                color = black6,
                fontSize = 13.sp,
                lineHeight = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(66.dp)
            )
            Text(
                text = "目前周长（米）",
                color = black6,
                fontSize = 13.sp,
                lineHeight = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 107.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ButtonWidget(
                text = "退出测量",
                type = ButtonWidgetType.Default,
                disabled = uiState.measureState == MeasureState.Stop,
                modifier = Modifier.size(106.dp, 36.dp),
                onTap = {}
            )
            Spacer(modifier = Modifier.size(22.dp))
            ButtonWidget(
                text = "保存结束",
                type = ButtonWidgetType.Primary,
                disabled = uiState.measureState == MeasureState.Stop,
                modifier = Modifier.size(106.dp, 36.dp),
                onTap = {}
            )
        }
    }
}