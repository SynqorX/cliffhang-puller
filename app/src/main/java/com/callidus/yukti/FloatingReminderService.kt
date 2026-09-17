package com.callidus.yukti

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat

class FloatingReminderService : Service() {

    private var windowManager: WindowManager? = null
    private var floatingView: View? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val taskName = intent?.getStringExtra("TASK_NAME") ?: "Complete your task!"

        startForegroundServiceNotification()
        showFloatingWindow(taskName)

        return START_NOT_STICKY
    }

    private fun startForegroundServiceNotification() {
        val channelId = "yukti_overlay_channel"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Yukti Active Alerts",
                NotificationManager.IMPORTANCE_LOW
            )
            manager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Yukti Active Alert")
            .setContentText("A task enforcement prompt is currently active.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(1001, notification)
    }

    private fun showFloatingWindow(taskName: String) {
        if (floatingView != null) return // Already showing

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val layoutParamsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutParamsType,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            y = 120
        }

        // Programmatic popup card
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#EE1E1E2E"))
            setPadding(48, 48, 48, 48)
            elevation = 20f
        }

        val titleText = TextView(this).apply {
            text = "Yukti Enforcement"
            setTextColor(Color.WHITE)
            textSize = 18f
            paint.isFakeBoldText = true
        }

        val bodyText = TextView(this).apply {
            text = taskName
            setTextColor(Color.parseColor("#CDD6F4"))
            textSize = 15f
            setPadding(0, 16, 0, 24)
        }

        val dismissButton = Button(this).apply {
            text = "Done & Dismiss"
            setBackgroundColor(Color.parseColor("#A6E3A1"))
            setTextColor(Color.BLACK)
            setOnClickListener {
                stopSelf()
            }
        }

        container.addView(titleText)
        container.addView(bodyText)
        container.addView(dismissButton)

        floatingView = container
        windowManager?.addView(floatingView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (floatingView != null) {
            windowManager?.removeView(floatingView)
            floatingView = null
        }
    }
}