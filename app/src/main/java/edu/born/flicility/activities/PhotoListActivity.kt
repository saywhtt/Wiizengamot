package edu.born.flicility.activities

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import edu.born.flicility.fragments.PhotoListFragment

class PhotoListActivity : AbstractFragmentActivity() {
    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, PhotoListActivity::class.java)
        }
    }

    override fun getFragment() = PhotoListFragment()
}