package edu.born.flicility.service

import android.app.job.JobInfo
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import edu.born.flicility.TAG
import java.util.concurrent.TimeUnit

const val JOB_ID = 1000

fun enqueueWork(context: Context, work: Intent) {
    JobIntentService.enqueueWork(context, PollService::class.java, JOB_ID, work)
}

class PollService: JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "Intent: $intent")
    }

}
