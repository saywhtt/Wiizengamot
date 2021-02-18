package edu.born.flicility.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import edu.born.flicility.databinding.FragmentPhotoWebPageBinding
import edu.born.flicility.fragments.abstraction.VisibleFragment

class PhotoWebPageFragment : VisibleFragment<FragmentPhotoWebPageBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoWebPageBinding =
            FragmentPhotoWebPageBinding::inflate

    companion object {
        private const val ARG_URI = "ARG_URI"
        fun newInstance(uri: Uri) = PhotoWebPageFragment().apply {
            arguments = bundleOf(
                    ARG_URI to uri
            )
        }
    }

    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uri = arguments?.getParcelable(ARG_URI) ?: throw IllegalArgumentException()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun setup() {
        binding.fragmentPhotoWebPagePb.max = 100
        with(binding.fragmentPhotoWebPageWv) {
            settings.javaScriptEnabled = true
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    with(binding.fragmentPhotoWebPagePb) {
                        if (newProgress == 100) visibility = View.GONE
                        else {
                            visibility = View.VISIBLE
                            progress = newProgress
                        }
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    val activity = activity as AppCompatActivity
                    activity.supportActionBar?.subtitle = title
                }
            }
            webViewClient = WebViewClient()
            loadUrl(uri.toString())
        }
    }
}