package edu.born.flicility.activities

import android.net.Uri
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*
import edu.born.flicility.R
import edu.born.flicility.fragments.PhotoListFragment
import edu.born.flicility.fragments.PhotoPagerFragment
import edu.born.flicility.fragments.PhotoSearchFragment
import edu.born.flicility.fragments.PhotoWebPageFragment

class SingleFragmentActivity : AppCompatActivity() {

    companion object {
        // keys
        const val START_SEARCH_REQUEST_KEY = "START_SEARCH_REQUEST_KEY"
        const val START_PHOTO_PAGER_REQUEST_KEY = "START_PHOTO_PAGER_REQUEST_KEY"
        const val CLOSE_FRAGMENT_REQUEST_KEY = "END_PHOTO_PAGER_REQUEST_KEY"
        const val END_PHOTO_PAGER_BY_ALL_REQUEST_KEY = "END_PHOTO_PAGER_BY_ALL_REQUEST_KEY"
        const val END_PHOTO_PAGER_BY_SEARCH_REQUEST_KEY = "END_PHOTO_PAGER_BY_SEARCH_REQUEST_KEY"

        //bundles
        const val PHOTOS_ARG = "PHOTOS_ARG"
        const val PHOTO_POSITION_ARG = "PHOTO_POSITION_ARG"
        const val QUERY_ARG = "QUERY_ARG"
        const val URI_ARG = "URI_ARG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        startMainFragment()
        prepareListeners()
    }

    private fun prepareListeners() {
        setFragmentResultListenerByKey(START_SEARCH_REQUEST_KEY,
                FragmentResultListener { _, _ -> replaceFragment<PhotoSearchFragment>() })
        setFragmentResultListenerByKey(START_PHOTO_PAGER_REQUEST_KEY,
                FragmentResultListener { _, result ->
                    val (photos, position, query) = PhotoPagerFragment.splitArguments(result)
                    val fragment = PhotoPagerFragment.newInstance(position, photos, query)
                    replaceFragment(fragment)
                })
        setFragmentResultListenerByKey(CLOSE_FRAGMENT_REQUEST_KEY,
                FragmentResultListener { _, _ -> supportFragmentManager.popBackStack() })
    }

    private fun setFragmentResultListenerByKey(key: String, listener: FragmentResultListener) =
            supportFragmentManager.setFragmentResultListener(key, this, listener)

    private fun startMainFragment() =
            with(supportFragmentManager) {
                findFragmentByTag(PhotoListFragment::class.java.name)
                        ?: addFragment<PhotoListFragment>()
            }

    private inline fun <reified T : Fragment> replaceFragment() {
        supportFragmentManager.commit {
            replace<T>(R.id.fragment_container)
            setReorderingAllowed(true)
            addToBackStack(T::class.java.name)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
            setReorderingAllowed(true)
            addToBackStack(fragment::class.java.name)
        }
    }

    private inline fun <reified T : Fragment> addFragment() {
        supportFragmentManager.commit {
            add<T>(R.id.fragment_container)
            setReorderingAllowed(true)
        }
    }

    @LayoutRes
    private fun getLayoutResId(): Int = R.layout.activity_fragment
}