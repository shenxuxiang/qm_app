package com.example.qm_app.components

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.qm_app.common.Overlay
import com.example.qm_app.ui.theme.black

@Composable
fun ImageWidget(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val view = LocalView.current
    val context = LocalContext.current
    var imageSize by remember { mutableStateOf(Size.Zero) }

    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = contentScale,
            onSuccess = {
                imageSize = Size(
                    it.result.drawable.intrinsicWidth.toFloat(),
                    it.result.drawable.intrinsicHeight.toFloat()
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = {
                    lateinit var dispose: () -> Unit
                    dispose = Overlay.create(view) {
                        PreviewImageWidget(url, imageSize, onClose = { dispose() })
                    }
                })
        )
    }
}

@Composable
fun PreviewImageWidget(url: String, imageSize: Size, onClose: () -> Unit) {
    val minScale = 1f
    val maxScale = 2.5f
    val view = LocalView.current
    val density = LocalDensity.current
    val context = LocalContext.current
    var scale by remember { mutableStateOf(1f) }
    var visiblePreview by remember { mutableStateOf(true) }
    var translation by remember { mutableStateOf(Offset.Zero) }
    val alpha by animateFloatAsState(targetValue = if (visiblePreview) 1f else 0f) {
        if (it <= 0f) onClose()
    }

    val onBackPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current

    DisposableEffect(Unit) {
        var onBackPressedCallback: OnBackPressedCallback? = null
        onBackPressedDispatcherOwner?.let {
            val onBackPressedDispatcher = it.onBackPressedDispatcher
            onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    visiblePreview = false
                }
            }
            onBackPressedDispatcher.addCallback(onBackPressedCallback)
        }
        
        onDispose { onBackPressedCallback?.remove() }
    }

    Box(
        modifier = Modifier
            .alpha(alpha)
            .fillMaxSize()
            .background(color = black)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { visiblePreview = false })
            }
            .pointerInput(Unit) {
                detectTransformGestures(
                    panZoomLock = true,
                    onGesture = { _, pan, zoom, _ ->
                        translation += pan
                        scale = (scale * zoom).coerceIn(minScale, maxScale)
                        if (scale * imageSize.width <= view.width) {
                            translation = translation.copy(x = 0f)
                        } else {
                            val maxOffsetX = (scale * imageSize.width - view.width) / 2
                            val minOffsetX = -1 * maxOffsetX
                            translation = Offset(
                                x = translation.x.coerceIn(minOffsetX, maxOffsetX),
                                y = translation.y,
                            )
                        }

                        if (scale * imageSize.height <= view.height) {
                            translation = translation.copy(y = 0f)
                        } else {
                            val maxOffsetY = (scale * imageSize.height - view.height) / 2
                            val minOffsetY = -1 * maxOffsetY
                            translation = Offset(
                                x = translation.x,
                                y = translation.y.coerceIn(minOffsetY, maxOffsetY),
                            )
                        }
                    }
                )
            }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .alpha(alpha)
                .fillMaxSize()
                .background(color = black)
                .wrapContentSize(unbounded = true)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(url)
                    .allowHardware(false)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .graphicsLayer(translationX = translation.x, translationY = translation.y)
                    .width(with(density) { (imageSize.width * scale).toDp() })
                    .height(with(density) { (imageSize.height * scale).toDp() })
            )
        }
    }
}