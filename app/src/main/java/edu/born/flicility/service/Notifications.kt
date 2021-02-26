package edu.born.flicility.service

import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import edu.born.flicility.R
import edu.born.flicility.activities.SingleFragmentActivity

const val UNIQUE_WORK_NAME = "POLL_SERVICE"
const val REQUEST_CODE = "REQUEST_CODE"
const val NOTIFICATION = "NOTIFICATION"
const val SHOW_NOTIFICATION = "edu.born.flicility.SHOW_NOTIFICATION"
const val PERMISSION_PRIVATE = "edu.born.flicility.PRIVATE"

const val NEW_PHOTO_NOTIFICATION_CHANNEL_ID = "newPhotoNotificationId"
const val NEW_PHOTO_NOTIFICATION_CHANNEL_DESCRIPTION = "New photos notification"

fun Context.getPreparedNotification(): Notification {
    val startActivityIntent = SingleFragmentActivity.newIntent(applicationContext)
    val pendingIntent = PendingIntent.getActivity(applicationContext, 0, startActivityIntent, 0)

    return NotificationCompat.Builder(applicationContext, NEW_PHOTO_NOTIFICATION_CHANNEL_ID)
            .setTicker(applicationContext.getString(R.string.new_photo_notification_title))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(applicationContext.getString(R.string.new_photo_notification_title))
            .setContentText(applicationContext.getString(R.string.new_photo_notification_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
}

fun Context.showNotification(notification: Notification) {
    val intent = Intent(SHOW_NOTIFICATION).putExtra(REQUEST_CODE, 0)
            .putExtra(NOTIFICATION, notification)

    applicationContext.sendOrderedBroadcast(
            intent,
            PERMISSION_PRIVATE,
            null,
            null,
            Activity.RESULT_OK,
            null,
            null
    )
}