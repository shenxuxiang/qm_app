package com.example.testactivity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.example.qm_app.MainActivity
import com.example.qm_app.R

@RequiresApi(Build.VERSION_CODES.O)
class QmLocationForegroundService : Service() {
    inner class ServiceBind : Binder() {
        val instance: QmLocationForegroundService get() = this@QmLocationForegroundService
    }

    companion object {
        private const val NOTIFY_ID = 1
        private const val CHANNEL_ID = "6335"
        private const val CHANNEL_NAME = "用户定位通知"
        private const val ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION
        private const val ACCESS_COARSE_LOCATION =
            android.Manifest.permission.ACCESS_COARSE_LOCATION

        private var intent: Intent? = null
        private var serviceConnection: ServiceConnection? = null
        fun bindService(context: Context, connection: ServiceConnection) {
            serviceConnection = connection
            intent = Intent(context, QmLocationForegroundService::class.java)
            context.bindService(intent!!, connection, BIND_AUTO_CREATE)
        }

        fun unbindService(context: Context) {
            if (intent != null && serviceConnection != null) {
                context.stopService(intent)
                context.unbindService(serviceConnection!!)
            }
        }
    }

    private lateinit var qmLocationManager: QmLocationManager
    private lateinit var notificationManager: NotificationManager

    private var updateLocationCallback: ((location: Location) -> Unit)? = null
    private var stopUpdateLocation: StopUpdateLocation? = null

    var isRunning = false
        private set

    fun startTracking(context: Context, block: (location: Location) -> Unit) {
        if (!isRunning) {
            throw RuntimeException("The QmLocationForegroundService Has Not Been Created Yet")
        } else {
            // 判断 updateLocationCallback 是否为空，避免重启动服务
            if (updateLocationCallback == null) {
                updateLocationCallback = block
                // 启动服务
                context.startService(intent)
            }
        }
    }

    fun stopTracking() {
        if (isRunning) {
            stopUpdateLocation?.let { it() }
            updateLocationCallback = null
            stopUpdateLocation = null
        }
    }

    private fun createNotification(message: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID).run {
            setContentText(message)
            setSmallIcon(R.drawable.logo)
            setContentIntent(pendingIntent)
            setContentTitle("正在跟踪用户轨迹")
            // 关键设置：允许在锁屏界面显示
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            build()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return ServiceBind();
    }

    override fun onCreate() {
        isRunning = true
        qmLocationManager = QmLocationManager(this)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH // 使用HIGH重要性级别
        ).apply {
            // 设置锁屏可见性
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            // 设置绕过免打扰模式
            setBypassDnd(true)
        }

        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
        );

        // 调用 startForeground() 后，表示着当前服务将成为一个前台服务
        startForeground(NOTIFY_ID, createNotification("开始跟踪用户的运动轨迹"))
        super.onCreate()
    }

    @RequiresPermission(allOf = [ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION])
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        stopUpdateLocation =
            qmLocationManager.updateLocation { location ->
                // 每当用户轨迹更新时，都会通过通知告知用户
                val message = "您当前的位置：经度：${location.longitude}，纬度：${location.latitude}"
                val notification = createNotification(message)
                notificationManager.notify(NOTIFY_ID, notification)
                updateLocationCallback?.let { it(location) }
            }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        intent = null
        isRunning = false
        serviceConnection = null
        stopUpdateLocation = null
        updateLocationCallback = null

        super.onDestroy()
    }
}
