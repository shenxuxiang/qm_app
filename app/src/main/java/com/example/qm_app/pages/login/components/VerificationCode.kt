package com.example.qm_app.pages.login.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.qm_app.ui.theme.primaryColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VerificationCode(onStart: (suspend () -> Boolean)) {
    // 初始倒计时长度(毫秒)
    val initialCountdown = 60000
    val coroutineScope = rememberCoroutineScope()
    var endTime by rememberSaveable { mutableStateOf(value = 0L) }

    var countdown by remember(endTime) {
        mutableStateOf(
            value = if (endTime == 0L) {
                0
            } else {
                ((endTime - System.currentTimeMillis()) / 1000)
                    .coerceAtLeast(minimumValue = 0)
                    .toInt()
            }
        )
    }

    LaunchedEffect(endTime) {
        launch {
            while (countdown > 0) {
                delay(timeMillis = 1000L)
                countdown--
            }
        }
    }

    if (countdown > 0) {
        Text(
            "${countdown}S",
            fontSize = 11.sp,
            lineHeight = 11.sp,
            color = primaryColor,
        )
    } else {
        Text(
            "获取验证码",
            fontSize = 11.sp,
            lineHeight = 11.sp,
            color = primaryColor,
            modifier =
                Modifier.clickable(
                    indication = null,
                    interactionSource = null,
                    onClick = {
                        coroutineScope.launch {
                            val result = onStart()
                            if (result) {
                                endTime = System.currentTimeMillis() + initialCountdown
                            }
                        }
                    }
                )
        )
    }
}