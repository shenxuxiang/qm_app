package com.example.qm_app.pages.camera

import androidx.camera.core.Camera
import androidx.camera.core.FocusMeteringAction
import androidx.camera.view.PreviewView
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.qm_app.ui.theme.white
import java.util.concurrent.TimeUnit

@Composable
fun FocusBox(previewView: PreviewView, camera: Camera?, previewHeight: Int, previewWidth: Int) {
    val context = LocalContext.current
    /* 白色矩形拍照区域 */
    val boxW = (previewWidth * 0.75).toInt()
    val boxH = (boxW / 0.63).toInt();

    var focusPosition by remember { mutableStateOf(Offset.Zero) }

    val infiniteTransition = rememberInfiniteTransition()
    val focusBoxColor = infiniteTransition.animateColor(
        Color(0xFFFF9900),
        targetValue = Color(0xFFFF7700),
        animationSpec = infiniteRepeatable(tween(300), RepeatMode.Reverse)
    )

    Box(
        modifier = Modifier
            .size(width = boxW.dp, height = boxH.dp)
            .border(BorderStroke(3.dp, white))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        /**
                         * 这里 offset 是相对于当前节点的坐标系偏移量，
                         * 而 MeteringPoint 应该是相对于父容器的坐标系进行计算才对
                         * distX、distY 分别表示当前节点相对于父容器的偏移量（x、y轴）
                         * */
                        val distY = ((previewHeight - boxH) / 2).dp.toPx()
                        val distX = ((previewWidth - boxW) / 2).dp.toPx()
                        val meteringPoint1 = previewView.meteringPointFactory.createPoint(
                            offset.x + distX,
                            offset.y + distY,
                        )

                        val action =
                            FocusMeteringAction.Builder(meteringPoint1)
                                .setAutoCancelDuration(10L, TimeUnit.SECONDS) // 3秒后自动失焦
                                // .disableAutoCancel() // 取消自动失去焦
                                .build()
                        focusPosition = offset

                        // 当相机聚焦后，应该让焦点矩形框隐藏起来，
                        camera!!.cameraControl.startFocusAndMetering(action).addListener(
                            { focusPosition = Offset.Zero },
                            ContextCompat.getMainExecutor(context)
                        )
                    }
                )
            }
    ) {
        /* 焦点框框， */
        if (focusPosition != Offset.Zero) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = focusPosition.x.toInt() - 30.dp.toPx().toInt(),
                            y = focusPosition.y.toInt() - 30.dp.toPx().toInt(),
                        )
                    }
                    .size(60.dp)
                    .border(width = 1.dp, color = focusBoxColor.value)
                    .align(Alignment.TopStart)
            )
        }
    }
}
