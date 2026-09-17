package com.callidus.yukti

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskName = intent.getStringExtra("TASK_NAME") ?: "Complete your scheduled task!"

        // 1. If overlay permission is granted, pop up over other apps
        if (Settings.canDrawOverlays(context)) {
            val serviceIntent = Intent(context, FloatingReminderService::class.java).apply {
                putExtra("TASK_NAME", taskName)
            }
            ContextCompat.startForegroundService(context, serviceIntent)
        }

        // 2. High-priority notification fallback
        val channelId = "yukti_reminders"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Yukti Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "High-priority notifications for task enforcement"
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Yukti Assistant")
            .setContentText(taskName)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
