package com.callidus.yukti

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val taskName = intent.getStringExtra("TASK_NAME") ?: "Complete your task!"
        val channelId = "yukti_reminders"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Required for Android 8.0 (API 26) and above
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

        // Build the alert with high priority to pop up as a heads-up notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Yukti Assistant")
            .setContentText(taskName)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .build()

        // Use a unique timestamp integer ID so multiple alarms don't overwrite each other
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}