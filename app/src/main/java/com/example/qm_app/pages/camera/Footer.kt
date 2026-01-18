package com.example.qm_app.pages.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.qm_app.common.QmIcons
import com.example.qm_app.components.Alert
import com.example.qm_app.components.QmIcon
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.black
import com.example.qm_app.ui.theme.corner6
import com.example.qm_app.ui.theme.white
import com.example.qm_app.utils.saveImageToGallery
import java.io.File

@Composable
fun Footer(modifier: Modifier = Modifier, imageCapture: ImageCapture?, onTakePicture: () -> Unit) {
    val context = LocalContext.current
    val anchorView = LocalView.current
    var photo by remember { mutableStateOf<Bitmap?>(null) }
    val photoFile = remember { File(context.externalCacheDir, "camera_x_farm_auth.jpeg") }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.then(Modifier.navigationBarsPadding()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (photo != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(photo)
                        .allowHardware(false)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(corner6)
                        .clickable(onClick = {
                            Alert.confirm(
                                content = { onClose ->
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(black)
                                            .clickable(onClick = { onClose() }),
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(context)
                                                .data(photo)
                                                .allowHardware(false)
                                                .crossfade(true)
                                                .build(),
                                            contentScale = ContentScale.FillWidth,
                                            contentDescription = null,
                                        )
                                    }
                                },
                                footer = {},
                                view = anchorView,
                                contentPadding = PaddingValues(0.dp)
                            )
                        })
                )
            } else {
                /* 占位，空的 */
                Box(modifier = Modifier.size(50.dp))
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, white, CircleShape),
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.Transparent, CircleShape)
                        .background(white)
                        .clickable(onClick = {
                            // if (photoFile.exists()) photoFile.delete()
                            val outputOptions =
                                ImageCapture.OutputFileOptions.Builder(photoFile).build()
                            // 拍照
                            imageCapture!!.takePicture(
                                outputOptions,
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                        outputFileResults.savedUri?.let {
                                            onTakePicture()
                                            photo = BitmapFactory.decodeStream(
                                                context.contentResolver.openInputStream(it)
                                            )
                                            saveImageToGallery(context, photoFile)
                                            photoFile.delete()
                                        }
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        exception.printStackTrace()
                                    }
                                })
                        }),
                )
            }
            /* 占位，空的 */
            Box(modifier = Modifier.size(50.dp), contentAlignment = Alignment.Center) {
                if (photo != null) {
                    QmIcon(
                        icon = QmIcons.Stroke.Success,
                        tint = white, // MaterialTheme.colorScheme.primary,
                        size = 36.dp,
                        modifier = Modifier.clickable(onClick = {
                            Router.controller.previousBackStackEntry?.savedStateHandle?.set(
                                "arguments",
                                photo
                            )
                            Router.popBackStack()
                        })
                    )
                } else {
                    QmIcon(
                        icon = QmIcons.Stroke.CloseCircle,
                        tint = white, // MaterialTheme.colorScheme.primary,
                        size = 36.dp,
                        modifier = Modifier.clickable(onClick = {
                            Router.popBackStack()
                        })
                    )
                }
            }
        }
    }
}