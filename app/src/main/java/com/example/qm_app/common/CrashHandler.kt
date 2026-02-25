package com.example.qm_app.common

import android.content.Context
import android.os.Build
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.qm_app.database.CrashLog
import com.example.qm_app.utils.uuid
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class CrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {
    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    override fun uncaughtException(t: Thread, e: Throwable) {
        saveCrashLog(e)
        defaultHandler?.uncaughtException(t, e)
    }

    fun saveCrashLog(throwable: Throwable) {
        val gson = Gson()
        val stackTrace = throwable.stackTraceToString()
        val deviceInfo = mapOf(
            "brand" to Build.BRAND,
            "model" to Build.MODEL,
            "sdk" to Build.VERSION.SDK_INT,
        )
        val file = File(context.externalCacheDir, "crash_reports/${uuid()}.json")
        val crashLog = CrashLog(deviceInfo = gson.toJson(deviceInfo), stackTrace = stackTrace)

        // 如果目录不存在，则创建目录
        if (!(file.parentFile?.exists() ?: false)) file.parentFile?.mkdirs()

        file.writeText(gson.toJson(crashLog))
    }

    fun uploadCrashLog() {
        /**
         * 用于创建一个约束条件构建器。
         * setRequiredNetworkType(NetworkType.CONNECTED) 指定这个任务只有在设备连接到网络时才能执行。
         * NetworkType.CONNECTED 表示有可用网络（Wi-Fi 或移动数据）。
         */
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        try {
            // 将前面定义好的约束条件应用到这个任务上，意味着只有满足所有约束时，任务才会真正执行
            val request = OneTimeWorkRequest.Builder(UploadCrashLogWorker::class)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueue(request)
        } catch (t: Throwable) {
            println(t.message)
        }
    }
}


class UploadCrashLogWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        File(context.externalCacheDir, "crash_reports").list()?.let { fileNames ->
            if (fileNames.size > 0) {
                LogUntil.d("正在执行 Crash Log 上报")
                for (fileName in fileNames) {
                    val file = File(context.externalCacheDir, "crash_reports/$fileName")
                    try {
                        /**
                         * 这里使用 FileReader()读取文件内容，为什么没有使用 openFileInput() ？？？
                         * 这是因为 openFileInput() 只能访问应用内部的存储的文件（/data/data/包名/files/目录下的文件）；
                         * 访问外部缓存目录（context.externalCacheDir）的文件请使用 FileReader()
                         * */

                        val read = BufferedReader(FileReader(file))
                        val stringBuilder = StringBuilder()
                        read.use {
                            read.forEachLine { line ->
                                stringBuilder.append(line)
                            }
                        }

                        println("===========================${fileName}:")
                        println("===========================$stringBuilder")
                        file.delete()
                    } catch (exception: Exception) {
                        LogUntil.d("Crash Log 上报失败")
                    }
                }
            }
        }

        return Result.success()
    }
}