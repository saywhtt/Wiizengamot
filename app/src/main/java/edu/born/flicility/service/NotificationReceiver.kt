package edu.born.flicility.service

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val requestCode = intent?.getIntExtra(REQUEST_CODE, 0) ?: 0
            val notification = intent?.getParcelableExtra<Notification>(NOTIFICATION)
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                        NEW_PHOTO_NOTIFICATION_CHANNEL_ID,
                        NEW_PHOTO_NOTIFICATION_CHANNEL_DESCRIPTION,
                        NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(notificationChannel)
            }

            notificationManager.notify(requestCode, notification)
        }
    }
}