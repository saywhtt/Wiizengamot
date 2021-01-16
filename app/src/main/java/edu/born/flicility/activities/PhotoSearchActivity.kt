package edu.born.flicility.activities

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import edu.born.flicility.fragments.PhotoSearchFragment

class PhotoSearchActivity : AbstractFragmentActivity() {
    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, PhotoSearchActivity::class.java)
        }
    }

    override fun getFragment(): Fragment = PhotoSearchFragment()
}