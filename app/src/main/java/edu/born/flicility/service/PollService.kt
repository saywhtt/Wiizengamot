package edu.born.flicility.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import edu.born.flicility.TAG
import edu.born.flicility.fragments.PhotoGalleryFragment

class PollService: JobIntentService() {

    companion object {
        fun newIntent(context: Context) = Intent(context, PollService::class.java)
    }

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "Intent: $intent")
    }
}
