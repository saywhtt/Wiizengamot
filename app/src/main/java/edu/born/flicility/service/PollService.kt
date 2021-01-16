package edu.born.flicility.service

import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import androidx.work.WorkInfo.State.*
import edu.born.flicility.R
import edu.born.flicility.activities.PhotoGalleryActivity
import edu.born.flicility.app.App
import edu.born.flicility.getLastResultId
import edu.born.flicility.model.Photo
import edu.born.flicility.network.PhotoService
import edu.born.flicility.setLastResultId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val TAG: String get() = PollService::class.java.simpleName

const val UNIQUE_WORK_NAME = "POLL_SERVICE"
const val REQUEST_CODE = "REQUEST_CODE"
const val NOTIFICATION = "NOTIFICATION"
const val SHOW_NOTIFICATION = "edu.born.flicility.SHOW_NOTIFICATION"
const val PERMISSION_PRIVATE = "edu.born.flicility.PRIVATE"

const val NEW_PHOTO_NOTIFICATION_CHANNEL_ID = "newPhotoNotificationId"
const val NEW_PHOTO_NOTIFICATION_CHANNEL_DESCRIPTION = "New photos notification"

fun setServiceStart(context: Context, isOn: Boolean) {
    val workManager = WorkManager.getInstance(context)
    if (isOn) {
        val work = PeriodicWorkRequestBuilder<PollService>(15, TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, work)
    } else {
        workManager.cancelUniqueWork(UNIQUE_WORK_NAME)
    }
}

fun isServiceStarted(context: Context): Boolean {
    var isStarted = false
    val workManager = WorkManager.getInstance(context)
    val workInfoList = workManager.getWorkInfosForUniqueWork(UNIQUE_WORK_NAME).get()
    workInfoList.forEach {
        if ((it.state == RUNNING) or (it.state == ENQUEUED))
            isStarted = true
    }
    return isStarted
}

class PollService(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    @Inject
    lateinit var photoService: PhotoService

    init {
        (applicationContext as App).appComponent.inject(this)
    }

    override fun doWork(): Result {
        return if (isNetworkAvailableAndConnected()) {
            val lastResultId = getLastResultId(applicationContext)
            photoService.getPhotos(page = 1, per_page = 1)
                    .enqueue(object : Callback<List<Photo>> {
                        override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                            response.body()?.let {
                                if (it.isEmpty()) return
                                else {
                                    val resultId = it[0].id
                                    if (resultId != lastResultId) {
                                        showNotification(getPreparedNotification())
                                        setLastResultId(applicationContext, resultId)
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Photo>>, t: Throwable) = t.printStackTrace()
                    })
            Result.success()
        } else {
            Result.retry()
        }
    }

    private fun isNetworkAvailableAndConnected(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                ?: false
    }

    private fun getPreparedNotification(): Notification {
        val startActivityIntent = PhotoGalleryActivity.newIntent(applicationContext)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, startActivityIntent, 0)

        return NotificationCompat.Builder(applicationContext, NEW_PHOTO_NOTIFICATION_CHANNEL_ID)
                .setTicker(applicationContext.getString(R.string.new_photo_notification_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(applicationContext.getString(R.string.new_photo_notification_title))
                .setContentText(applicationContext.getString(R.string.new_photo_notification_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
    }

    private fun showNotification(notification: Notification) {
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

}