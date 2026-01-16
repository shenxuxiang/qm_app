package com.example.qm_app.pages.camera


import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.qm_app.ui.theme.black
import com.example.qm_app.ui.theme.primaryColor
import com.example.qm_app.ui.theme.white
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

private const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA

@Composable
fun CameraScreen() {
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
        if (showCamera.value && isGrantedPermission.value) CameraLayouts()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraLayouts() {
    val view = LocalView.current
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var photo by remember { mutableStateOf<Bitmap?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    val statusBarHeight = WindowInsets.statusBars.getTop(LocalDensity.current)
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }
    val focusPosition = remember { mutableStateOf(Offset.Zero) }

    var maxZoom = remember { 1f }
    var minZoom = remember { 1f }
    var currentZoom = remember { 1f }
    var camera by remember { mutableStateOf<Camera?>(null) }

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

                imageCapture =
                    ImageCapture.Builder().build()

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


//        previewView.setOnTouchListener { v, event ->
//            val meteringPoint1 =
//                previewView.meteringPointFactory.createPoint(event?.x ?: 0f, event?.y ?: 0f)
//            val action = FocusMeteringAction.Builder(meteringPoint1) // default AF|AE|AWB
//                .setAutoCancelDuration(3, TimeUnit.SECONDS)
//                .build()
//
//            println("${event?.x ?: 0f}, ${event?.y ?: 0f}")
//            camera!!.cameraControl.startFocusAndMetering(action).addListener(
//                {
//                    println("=========================OK")
//                },
//                ContextCompat.getMainExecutor(context)
//            )
//            true
//        }
    }


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
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
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize(),
        )
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val boxW = (screenWidth * 0.75).toInt()
        val boxH = (boxW / 0.63).toInt();

        Box(
            modifier = Modifier
                .size(width = boxW.dp, height = boxH.dp)
                .border(BorderStroke(3.dp, white))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val meteringPoint1 =
                                previewView.meteringPointFactory.createPoint(offset.x, offset.y)
                            val action =
                                FocusMeteringAction.Builder(meteringPoint1) // default AF|AE|AWB
                                    .setAutoCancelDuration(3, TimeUnit.SECONDS)
                                    .build()
                            focusPosition.value = offset
                            camera!!.cameraControl.startFocusAndMetering(action).addListener(
                                {
                                    println("=========================OK")
                                },
                                ContextCompat.getMainExecutor(context)
                            )
                        }
                    )
                }
//                .clickable(onClick = {
//                    imageCapture?.takePicture(
//                        ContextCompat.getMainExecutor(context),
//                        object : ImageCapture.OnImageCapturedCallback() {
//                            override fun onCaptureSuccess(image: ImageProxy) {
//                                val bitmap = image.toBitmap()
//                                photo = bitmap
//                            }
//
//                            override fun onError(exception: ImageCaptureException) {
//                                super.onError(exception)
//                                exception.printStackTrace()
//                            }
//                        }
//                    )
//                })
        ) {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = focusPosition.value.x.toInt() - 30.dp.toPx().toInt(),
                            y = focusPosition.value.y.toInt() - 30.dp.toPx().toInt(),
                        )
                    }
                    .size(60.dp)
                    .border(width = 1.dp, color = primaryColor)
                    .align(Alignment.TopStart)
            )
        }





        if (photo != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(photo)
                    .allowHardware(false)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = {
                        println("====================oinclick")
                        photo = null
                    })
            )
        }
    }
}


@Composable
fun CameraLayout() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraController.bindToLifecycle(lifecycleOwner)
            cameraController.enableTorch(false)

            // 设置控制器
            controller = cameraController
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { previewView }
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(color = primaryColor)
        )
    }
}
