package edu.born.flicility.service

import android.content.Context
import androidx.work.*
import androidx.work.WorkInfo.State.ENQUEUED
import androidx.work.WorkInfo.State.RUNNING
import edu.born.flicility.app.App
import edu.born.flicility.getLastResultId
import edu.born.flicility.network.PhotoService
import edu.born.flicility.setLastResultId
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PollWorkerService(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        fun setServiceStart(context: Context, isOn: Boolean) {
            val workManager = WorkManager.getInstance(context)
            if (isOn) {
                val work = PeriodicWorkRequestBuilder<PollWorkerService>(15, TimeUnit.MINUTES).build()
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
    }

    @Inject
    lateinit var photoService: PhotoService

    init {
        (applicationContext as App).appComponent.inject(this)
    }

    override fun doWork(): Result = with(applicationContext) {
        return if (isNetworkAvailableAndConnected()) {
            val lastResultId = getLastResultId(this@with)
            photoService.getPhotos(page = 1, per_page = 1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        if (it.isNotEmpty()) {
                            val resultId = it[0].id
                            if (resultId != lastResultId) {
                                showNotification(getPreparedNotification())
                                setLastResultId(this@with, resultId)
                            }
                        }
                    }, {
                        it.printStackTrace()
                    })
            Result.success()
        } else {
            Result.retry()
        }
    }
}
