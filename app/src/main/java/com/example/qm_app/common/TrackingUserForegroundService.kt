package com.example.qm_app.common

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
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.qm_app.MainActivity
import com.example.qm_app.R

class TrackingUserForegroundService : Service() {
    inner class ServiceBind : Binder() {
        val instance: TrackingUserForegroundService get() = this@TrackingUserForegroundService
    }

    companion object {
        private const val NOTIFY_ID = 1
        private const val CHANNEL_ID = "6335"
        private const val CHANNEL_NAME = "正在跟踪用户轨迹"

        private var ctx: Context? = null
        private var intent: Intent? = null
        private var serviceConnection: ServiceConnection? = null

        /**
         * 绑定 service
         * */
        fun bindService(context: Context, connection: ServiceConnection) {
            ctx = context
            serviceConnection = connection
            intent = Intent(context, TrackingUserForegroundService::class.java)
            context.bindService(intent!!, connection, BIND_AUTO_CREATE)
        }

        /**
         * 解绑 service
         * */
        fun unbindService() {
            ctx?.unbindService(serviceConnection!!)
            ctx?.stopService(intent)

            ctx = null
            intent = null
            serviceConnection = null
        }
    }


    override fun onBind(intent: Intent?): IBinder {
        return ServiceBind()
    }

    private var pendingIntent: PendingIntent? = null
    private var updateTracking: ((Location) -> Unit)? = null
    private lateinit var notificationManager: NotificationManager

    private var cancelRequestLocationUpdates: (() -> Unit)? = null


    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(channel)

        startForeground(NOTIFY_ID, createNotification("开始跟踪用户轨迹···"))
    }

    private fun createNotification(message: String): Notification {
        if (pendingIntent == null) {
            val intent = Intent(this, MainActivity::class.java)
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID).run {
            setAutoCancel(true)
            setContentText(message)
            setSmallIcon(R.drawable.logo)
            setContentIntent(pendingIntent)
            setContentTitle("正在跟踪用户轨迹")
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            build()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 开始追踪
     * */
    fun startTracking(black: (Location) -> Unit) {
        updateTracking = black
        ctx?.let {
            startService(intent)
            cancelRequestLocationUpdates = UserLocationManager.requestLocationUpdates { location ->
                updateTracking?.invoke(location)
                val message = "您当前的位置：经度：${location.longitude}，纬度：${location.latitude}"
                notificationManager.notify(NOTIFY_ID, createNotification(message))
            }
        } ?: run {
            throw RuntimeException("The TrackingUserForegroundService Has Not Been Created Yet")
        }
    }

    /**
     * 停止追踪
     * */
    fun stopTracking() {
        cancelRequestLocationUpdates?.invoke()
        cancelRequestLocationUpdates = null
        updateTracking = null
    }
}