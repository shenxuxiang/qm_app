package com.example.qm_app.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.annotation.DrawableRes
import com.example.qm_app.common.QmAppConfig
import com.example.qm_app.common.QmApplication
import com.example.qm_app.pages.main.RegionSourceTree
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.abs
import kotlin.math.min

fun isBlankFrame(bitmap: Bitmap, threshold: Int = 32): Boolean {
    if (bitmap.width <= 0 || bitmap.height <= 0) return true
    // 采样检测，提高性能
    val sampleWidth = min(bitmap.width, 16)
    val sampleHeight = min(bitmap.height, 16)

    // 创建新的位图，如果指定的宽高与源图的宽度和高度相同，则返回源图，不创建新位图。
    val scaledBitmap = Bitmap.createScaledBitmap(
        bitmap,
        sampleWidth,
        sampleHeight,
        false
    )


    val pixels = IntArray(sampleWidth * sampleHeight)
    scaledBitmap.getPixels(pixels, 0, sampleWidth, 0, 0, sampleWidth, sampleHeight)

    // 计算平均颜色
    var totalR = 0
    var totalG = 0
    var totalB = 0

    for (pixel in pixels) {
        totalR += android.graphics.Color.red(pixel)
        totalG += android.graphics.Color.green(pixel)
        totalB += android.graphics.Color.blue(pixel)
    }

    val avgR = totalR / pixels.size
    val avgG = totalG / pixels.size
    val avgB = totalB / pixels.size

    // 检查像素值是否接近平均值（判断是否为单色）
    var uniformCount = 0
    for (pixel in pixels) {
        val r = android.graphics.Color.red(pixel)
        val g = android.graphics.Color.green(pixel)
        val b = android.graphics.Color.blue(pixel)

        if (abs(r - avgR) <= threshold &&
            abs(g - avgG) <= threshold &&
            abs(b - avgB) <= threshold
        ) {
            uniformCount++
        }
    }

    // 如果超过90%的像素颜色相近，认为是空白帧
    val isUniform = uniformCount.toFloat() / pixels.size > 0.9f

    // 检查是否为极暗或极亮
    val brightness = (avgR * 0.299 + avgG * 0.587 + avgB * 0.114)
    val isExtremeDark = brightness < 10
    val isExtremeBright = brightness > 245

    // 释放掉新创建的位图
    scaledBitmap.recycle()

    return isUniform || isExtremeDark || isExtremeBright
}

fun bitmapToBase64(bitmap: Bitmap, quality: Int = 85): String {
    val byteArrayOutputStream = ByteArrayOutputStream()

    try {
        // 压缩 Bitmap 为 JPEG
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        // 转换为 Base64
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } finally {
        try {
            byteArrayOutputStream.close()
        } catch (e: Exception) {
            // 忽略关闭异常
        }
    }
}

fun getFirstFrameVideo(context: Context, uri: Uri, quality: Int): String {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, uri)

    var step = 0L
    var bitmap: Bitmap? = null
    while (true) {
        bitmap = retriever.getFrameAtTime(step, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        if (bitmap != null && !isBlankFrame(bitmap)) {
            break
        } else {
            bitmap?.recycle()
            step += 1000
        }
    }
    return bitmapToBase64(bitmap, quality)
}

fun getNetworkAssetURL(path: String): String {
    return "${QmAppConfig.baseURL}${path}"
}

/**
 * 加载 /app/src/main/assets 中的文件资源
 * */
suspend fun loadAssetsFile(fileName: String): List<RegionSourceTree> =
    suspendCoroutine { continuation ->
        CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
            try {
                val context = QmApplication.context
                val assetManager = context.assets
                val inputStream = assetManager.open(fileName)
                val read = BufferedReader(InputStreamReader(inputStream))
                val json = StringBuilder()

                read.use {
                    read.forEachLine {
                        json.append(it)
                    }
                }

                val gson = Gson()
                val type = object : TypeToken<List<RegionSourceTree>>() {}
                continuation.resume(gson.fromJson(json.toString(), type))
            } catch (_: Exception) {
                continuation.resumeWithException(RuntimeException("数据加载失败"))
            }
        }
    }

/**
 * 生成 uuid
 * */
fun uuid(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
    return "${sdf.format(System.currentTimeMillis())}-${Math.random().toString().substring(2)}"
}


/**
 * 将文件保存到本地相册
 * */
fun saveImageToGallery(context: Context, sourceFile: File) {
    val displayName = "qm_app_${uuid()}.jpeg"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val contentResolver = context.contentResolver
    val imageUri = contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ) ?: return

    contentResolver.openOutputStream(imageUri)?.use { outputStream ->
        sourceFile.inputStream().use { inputStream ->
            inputStream.copyTo(outputStream)
        }
    }

    // 删除临时文件
    // sourceFile.delete()
}

fun dpToPx(dp: Int): Float {
    val metrics = QmApplication.context.resources.displayMetrics
    return (dp * metrics.density)
}

fun dpToPx(dp: Float): Float {
    val metrics = QmApplication.context.resources.displayMetrics
    return (dp * metrics.density)
}

/**
 * 旋转图像
 * */
fun rotateImage(rotate: Float, image: Bitmap): Bitmap {
    val matrix = android.graphics.Matrix().apply {
        postRotate(rotate)
    }

    return Bitmap.createBitmap(
        image,
        0,
        0,
        image.width,
        image.height,
        matrix,
        true,
    )
}

fun rotateImage(rotate: Float, @DrawableRes image: Int): Bitmap {
    val source = BitmapFactory.decodeResource(QmApplication.context.resources, image)
    val matrix = android.graphics.Matrix().apply {
        postRotate(rotate)
    }

    return Bitmap.createBitmap(
        source,
        0,
        0,
        source.width,
        source.height,
        matrix,
        true,
    )
}