package edu.born.flicility.activities

import android.content.Context
import android.content.Intent
import edu.born.flicility.fragments.PhotoFragment

class PhotoPagerActivity: AbstractFragmentActivity() {
    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, PhotoPagerActivity::class.java)
        }
    }

    override fun getFragment() = PhotoFragment()
}