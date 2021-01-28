package edu.born.flicility.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import edu.born.flicility.R
import edu.born.flicility.fragments.PhotoFragment
import edu.born.flicility.model.Photo

class PhotoPagerActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_PHOTO_LIST = "PHOTO_LIST"
        fun newIntent(context: Context?, photos: ArrayList<Photo>) =
                Intent(context, PhotoPagerActivity::class.java).putParcelableArrayListExtra(EXTRA_PHOTO_LIST, photos)
    }

    private lateinit var viewPager: ViewPager
    private lateinit var photos: List<Photo>

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_photo_pager)

        photos = intent.getParcelableArrayListExtra<Photo>(EXTRA_PHOTO_LIST) as List<Photo>

        viewPager = findViewById(R.id.photo_view_pager)
        viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int) = PhotoFragment.newInstance(photos[position])
            override fun getCount() = photos.size
        }
    }
}