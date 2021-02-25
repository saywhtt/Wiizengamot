package edu.born.flicility.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import edu.born.flicility.R
import edu.born.flicility.fragments.PhotoWebPageFragment

class PhotoWebPageActivity : AppCompatActivity(R.layout.activity_fragment) {

    companion object {
        private const val URI_ARG = "URI_ARG"
        fun newIntent(context: Context?, uri: Uri): Intent {
            return Intent(context, PhotoWebPageActivity::class.java).putExtra(URI_ARG, uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = requireNotNull(intent.getParcelableExtra<Uri>(SingleFragmentActivity.URI_ARG))
        val fragment = PhotoWebPageFragment.newInstance(uri)
        startFragment(fragment)
    }

    private fun startFragment(fragment: Fragment) =
            with(supportFragmentManager) {
                findFragmentByTag(PhotoWebPageFragment::class.java.name)
                        ?: addFragment<PhotoWebPageFragment>(fragment)
            }

    private inline fun <reified T : Fragment> addFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            add(R.id.fragment_container, fragment)
            setReorderingAllowed(true)
        }
    }
}