package edu.born.flicility.fragments.abstraction

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import edu.born.flicility.R
import edu.born.flicility.app.App

/*import edu.born.flicility.service.PERMISSION_PRIVATE
import edu.born.flicility.service.SHOW_NOTIFICATION*/

abstract class VisibleFragment<VB : ViewBinding> : ViewBindingFragment<VB>() {

    protected lateinit var app: App
    protected var blockReCreatedFromBackStack = false
    protected val actionBar: ActionBar?
        get() = (activity as AppCompatActivity).supportActionBar

    private val offNotificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            resultCode = Activity.RESULT_CANCELED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        app = activity?.applicationContext as App
    }

    override fun onResume() {
        super.onResume()
        /*activity?.registerReceiver(
                offNotificationReceiver,
                IntentFilter(SHOW_NOTIFICATION),
                PERMISSION_PRIVATE,
                null
        )*/
    }

    override fun onPause() {
        super.onPause()
        //  activity?.unregisterReceiver(offNotificationReceiver)
    }

    protected open fun hideKeyboard() {
        activity?.let {
            val imm = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = it.currentFocus ?: View(it)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}