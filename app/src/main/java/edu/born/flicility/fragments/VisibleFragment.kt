package edu.born.flicility.fragments

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.fragment.app.Fragment
import edu.born.flicility.service.PERMISSION_PRIVATE
import edu.born.flicility.service.SHOW_NOTIFICATION

open class VisibleFragment : Fragment() {
    private val offNotificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            resultCode = Activity.RESULT_CANCELED
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.registerReceiver(
                offNotificationReceiver,
                IntentFilter(SHOW_NOTIFICATION),
                PERMISSION_PRIVATE,
                null
        )
    }

    override fun onPause() {
        super.onPause()
        activity?.unregisterReceiver(offNotificationReceiver)
    }
}