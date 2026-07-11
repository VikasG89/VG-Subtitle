package com.vg.subtitle.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.vg.subtitle.R

class NotificationHelper(private val context: Context) {
    fun ensureChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "Subtitle processing", NotificationManager.IMPORTANCE_LOW)
        context.getSystemService<NotificationManager>()?.createNotificationChannel(channel)
    }

    fun progress(title: String, progress: Int): Notification {
        ensureChannel()
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.vgsubtitle_ic_notification)
            .setContentTitle(title)
            .setContentText("$progress%")
            .setOngoing(true)
            .setProgress(100, progress.coerceIn(0, 100), false)
            .build()
    }

    companion object {
        const val CHANNEL_ID = "vgsubtitle_processing"
        const val NOTIFICATION_ID = 4206
    }
}
