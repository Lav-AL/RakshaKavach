package com.rakshakavach.app.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.rakshakavach.app.R
import com.rakshakavach.app.ui.home.MainActivity
import java.util.Calendar
import java.util.concurrent.TimeUnit

class SafetyReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        sendSafetyReminder()
        return Result.success()
    }

    private fun sendSafetyReminder() {
        createNotificationChannel()

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val messages = listOf(
            "🦺 Start-of-Day Safety Check! Wear your PPE before beginning work today.",
            "⛑️ Safety First! Complete your gear checklist before starting your shift.",
            "🔒 Raksha-Kavach Reminder: Your safety gear protects YOUR life. Check it now!",
            "⚠️ Daily Safety Alert: No task is worth your safety. Gear up before you start!",
            "🛡️ Good Morning! Your PPE is your armor. Put it on before work begins."
        )
        val message = messages.random()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_shield)
            .setContentTitle("🚨 Raksha-Kavach Safety Alert")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(0xFFFFD600.toInt())
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Safety Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily start-of-day safety reminders"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "safety_reminder_channel"
        const val NOTIFICATION_ID = 1001
        const val WORK_TAG = "daily_safety_reminder"

        fun scheduleDailyReminder(context: Context) {
            // Schedule for 7:00 AM daily
            val now = Calendar.getInstance()
            val target = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 7)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
            }
            val delay = target.timeInMillis - now.timeInMillis

            val request = PeriodicWorkRequestBuilder<SafetyReminderWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(WORK_TAG)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                request
            )
        }

        fun sendImmediateReminder(context: Context) {
            val request = OneTimeWorkRequestBuilder<SafetyReminderWorker>()
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }
}
