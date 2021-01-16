package edu.born.flicility.activities

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import edu.born.flicility.fragments.PhotoGalleryFragment

class PhotoGalleryActivity : AbstractFragmentActivity() {
    companion object {
        fun newIntent(context: Context?): Intent {
            return Intent(context, PhotoGalleryActivity::class.java)
        }
    }

    override fun getFragment(): Fragment {
        return PhotoGalleryFragment()
    }
}