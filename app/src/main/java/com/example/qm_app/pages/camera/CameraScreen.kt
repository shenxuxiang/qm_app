package com.example.qm_app.pages.camera


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalZeroShutterLag
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_ZERO_SHUTTER_LAG
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.qm_app.ui.theme.black
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

@Composable
fun CameraScreen(requestType: String) { // 请求类型，在拍摄成功后，返回给上一个页面，用来识别是哪种请求
    val context = LocalContext.current
    val showCamera = rememberSaveable { mutableStateOf(false) }

    // 是否已经被授予权限
    val isGrantedPermission = rememberSaveable {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                CAMERA_PERMISSION,
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // 请求相机访问权限
    val requestCameraPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            isGrantedPermission.value = isGranted
        }

    val systemUiController = rememberSystemUiController()

    LaunchedEffect(systemUiController) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false,
        )
    }

    /**
     * 延迟 400ms 的作用：CameraX 初始化时比较耗性能，到导致页面转场动画执行比较卡顿，
     * 延迟的目的是为了等转场动画执行结束后，在执行 CameraX 初始化功能。
     * */
    LaunchedEffect(Unit) {
        if (!showCamera.value) {
            delay(400)
            showCamera.value = true
        }
    }

    // 如果没有被授予访问权限，则立即请求权限
    LaunchedEffect(Unit) {
        if (!isGrantedPermission.value) requestCameraPermission.launch(CAMERA_PERMISSION)
    }

    Surface(
        color = black,
        modifier = Modifier.fillMaxSize(),
    ) {
        if (showCamera.value && isGrantedPermission.value) CameraLayouts(requestType)
    }
}

@androidx.annotation.OptIn(ExperimentalZeroShutterLag::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraLayouts(requestType: String) {
    val view = LocalView.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val screenWidth = LocalConfiguration.current.screenWidthDp

    val animateFloat = remember { Animatable(0f) }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var flashMode by remember { mutableStateOf(FLASH_MODE_OFF) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER // 预览画面缩放模式
            implementationMode = PreviewView.ImplementationMode.PERFORMANCE // 渲染预览流模式
        }
    }
    var currentZoom = remember { 1f }
    var maxZoom by remember { mutableStateOf(1f) }
    var minZoom by remember { mutableStateOf(1f) }

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider {
                        surfaceProvider = previewView.surfaceProvider
                    }
                }

                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(CAPTURE_MODE_ZERO_SHUTTER_LAG) // 启用零快门延迟
                    .setTargetRotation(view.display.rotation) // 设置拍摄角度
                    .setFlashMode(FLASH_MODE_OFF) // 关闭闪光灯
                    .build()

                val useCaseGroup = UseCaseGroup.Builder()
                    .addUseCase(preview)
                    .addUseCase(imageCapture!!)
                    .build()

                cameraProvider.unbindAll()
                // 绑定生命周期
                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    useCaseGroup,
                )

                // 变焦的最大和最小 zoom。
                minZoom = camera!!.cameraInfo.zoomState.value?.minZoomRatio ?: 1f
                maxZoom = camera!!.cameraInfo.zoomState.value?.maxZoomRatio ?: 1f

                currentZoom = camera!!.cameraInfo.zoomState.value?.zoomRatio ?: 1f
            },
            ContextCompat.getMainExecutor(context)
        )
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Header(flashMode = flashMode, modifier = Modifier.weight(1f)) {
            if (flashMode == FLASH_MODE_ON) {
                flashMode = FLASH_MODE_OFF
                imageCapture!!.flashMode = FLASH_MODE_OFF
            } else {
                flashMode = FLASH_MODE_ON
                imageCapture!!.flashMode = FLASH_MODE_ON
            }
        }
        /* 相机预览部分，宽高比 3:4（因为拍摄图像的宽高比默认就是 3:4） */
        val previewHeight = (screenWidth * 1.333).toInt()
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(previewHeight.dp)
                .clipToBounds() // 因为最后一个 Box 不能严丝合缝的覆盖容器，所以这里加一个按照边界裁剪，这样才能完全覆盖
                .pointerInput(Unit) {
                    detectTransformGestures(
                        panZoomLock = true,
                        onGesture = { _, _, zoom, _ ->
                            currentZoom = (currentZoom * zoom).coerceIn(minZoom, maxZoom)
                            camera!!.cameraControl.setZoomRatio(currentZoom)
                        }
                    )
                },
        ) {
            /* 相机预览 */
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize(),
            )
            /* 聚焦矩形框 */
            FocusBox(
                camera = camera,
                previewView = previewView,
                previewWidth = screenWidth,
                previewHeight = previewHeight,
            )
            /* 遮罩（拍照时，屏幕需要闪一下，来模拟拍照的效果） */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(previewHeight.dp)
                    .alpha(animateFloat.value)
                    .background(color = black)
            )
        }
        Footer(
            requestType,
            imageCapture = imageCapture,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            coroutineScope.launch {
                animateFloat.animateTo(1f, animationSpec = tween(200))
                animateFloat.animateTo(0f, animationSpec = tween(200))
            }
        }
    }
}

