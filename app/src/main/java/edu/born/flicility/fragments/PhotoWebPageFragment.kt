package edu.born.flicility.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import edu.born.flicility.R
import edu.born.flicility.databinding.FragmentPhotoWebPageBinding
import edu.born.flicility.fragments.abstraction.VisibleFragment

class PhotoWebPageFragment : VisibleFragment<FragmentPhotoWebPageBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoWebPageBinding =
            FragmentPhotoWebPageBinding::inflate

    companion object {
        private const val URI_ARG = "URI_ARG"
        fun newInstance(uri: Uri) = PhotoWebPageFragment().apply {
            arguments = bundleOf(
                    URI_ARG to uri
            )
        }
    }

    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        uri = requireNotNull(arguments?.getParcelable(URI_ARG))
        actionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_close)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setup() {
        binding.fragmentPhotoWebPagePb.max = 100
        with(binding.fragmentPhotoWebPageWv) {
            settings.javaScriptEnabled = true
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (!blockAsyncCalls) {
                        with(binding.fragmentPhotoWebPagePb) {
                            if (newProgress == 100) visibility = View.GONE
                            else {
                                visibility = View.VISIBLE
                                progress = newProgress
                            }
                        }
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    val activity = activity as AppCompatActivity
                    activity.supportActionBar?.subtitle = getString(R.string.web_view_subtitle)
                }
            }
            webViewClient = WebViewClient()
            loadUrl(uri.toString())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            actionBar?.setDisplayHomeAsUpEnabled(false)
            activity?.finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}