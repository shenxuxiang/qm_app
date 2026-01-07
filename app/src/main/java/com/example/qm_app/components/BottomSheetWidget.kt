package com.example.qm_app.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.qm_app.ui.theme.corner10

/**
 * BottomSheet 底部模态框
 * dismissOnBackPress - 是否允许点击物理返回键关闭 Modal
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetWidget(
    open: Boolean,
    shape: Shape = corner10,
    onOpen: () -> Unit = {},
    onClose: () -> Unit = {},
    dismissOnBackPress: Boolean = false,
    dismissOnClickOutside: Boolean = false,
//    content: @Composable (onConfirmClose: (value: Boolean) -> Unit) -> Unit,
    content: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val handleOpenCallback = rememberUpdatedState(onOpen)
    // 是否确认关闭 Modal
    var confirmClose by rememberSaveable { mutableStateOf(value = false) }
    // 注意，confirmValueChange 对 dismissOnBackPress 没有影响。
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        // confirmValueChange = { true }
    )

    LaunchedEffect(key1 = open) {
        if (open) {
            handleOpenCallback.value()
//            confirmClose = false
        }
    }

//    fun onConfirmClose(value: Boolean) {
//        confirmClose = value
//        if (value) {
//            coroutineScope.launch {
//                sheetState.hide()
//                onClose()
//            }
//        }
//    }

    if (open)
        ModalBottomSheet(
            shape = shape,
            sheetState = sheetState,
            // 用户点击空白处关闭 Modal、以及通过物理返回键关闭 Modal 时都会触发该回调函数，
            // 用户手动关闭时，是通过 onConfirmClose 方法关闭的，不会触发该回调函数，
            onDismissRequest = onClose,
            properties = ModalBottomSheetProperties(
                shouldDismissOnBackPress = dismissOnBackPress,
            ),
            dragHandle = { // 将高度设置为 0，这样所有界面绘制都将在 content 中绘制
                BottomSheetDefaults.DragHandle(
                    height = 0.dp,
                    color = Color.Transparent,
                    modifier = Modifier
                        .padding(0.dp)
                        .height(0.dp), // 必须要设置 height 修饰符，设置 height 属性不管用
                )
            }
        ) {
//            content { onConfirmClose(value = it) }
            content()

        }
}