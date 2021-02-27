package edu.born.flicility.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import edu.born.flicility.R
import edu.born.flicility.fragments.PhotoListFragment
import edu.born.flicility.fragments.PhotoPagerFragment
import edu.born.flicility.fragments.PhotoSearchFragment
import edu.born.flicility.utils.*

class SingleFragmentActivity : AppCompatActivity() {

    companion object {
        //bundles
        const val PHOTOS_ARG = "PHOTOS_ARG"
        const val PHOTO_POSITION_ARG = "PHOTO_POSITION_ARG"
        const val QUERY_ARG = "QUERY_ARG"
        const val URI_ARG = "URI_ARG"

        fun newIntent(context: Context?): Intent {
            return Intent(context, SingleFragmentActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        startMainFragment()
        prepareListeners()
    }

    private fun prepareListeners() {
        setFragmentResultListener(START_SEARCH_REQUEST_KEY) { _, _ ->
            replaceFragment<PhotoSearchFragment>(getLayoutResId())
        }
        setFragmentResultListener(START_PHOTO_PAGER_REQUEST_KEY) { _, result ->
            val (photos, position, query) = PhotoPagerFragment.splitArguments(result)
            val fragment = PhotoPagerFragment.newInstance(position, photos, query)
            replaceFragment(fragment, getLayoutResId())
        }
        setFragmentResultListener(CLOSE_FRAGMENT_REQUEST_KEY) { _, _ ->
            supportFragmentManager.popBackStack()
        }
    }

    private fun startMainFragment() =
            with(supportFragmentManager) {
                findFragmentByTag(PhotoListFragment::class.java.name)
                        ?: addFragment<PhotoListFragment>(getLayoutResId(), false)
            }

    @LayoutRes
    private fun getLayoutResId(): Int = R.layout.activity_fragment
}