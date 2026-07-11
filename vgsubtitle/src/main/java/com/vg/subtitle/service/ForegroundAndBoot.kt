package com.vg.subtitle.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.work.WorkManager
import com.vg.subtitle.notification.NotificationHelper

class ForegroundSubtitleService : Service() {
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        startForeground(
            NotificationHelper.NOTIFICATION_ID,
            notificationHelper.progress("VG subtitle processing", 0),
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val progress = intent?.getIntExtra(EXTRA_PROGRESS, 0) ?: 0
        startForeground(
            NotificationHelper.NOTIFICATION_ID,
            notificationHelper.progress("VG subtitle processing", progress),
        )
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val EXTRA_PROGRESS = "progress"
    }
}

class BootRestartReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            WorkManager.getInstance(context).pruneWork()
        }
    }
}
