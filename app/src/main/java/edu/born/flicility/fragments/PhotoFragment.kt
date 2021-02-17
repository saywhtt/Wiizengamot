package edu.born.flicility.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import com.squareup.picasso.Picasso
import edu.born.flicility.R
import edu.born.flicility.databinding.FragmentPhotoBinding
import edu.born.flicility.model.Photo
import edu.born.flicility.views.PhotoView

class PhotoFragment : VisibleFragment(), PhotoView {

    interface Callback {
        fun loadUrl(uri: Uri)
    }

    companion object {
        private const val ARG_PHOTO = "ARG_PHOTO"
        fun newInstance(photo: Photo) =
                PhotoFragment().apply {
                    arguments = bundleOf(
                            ARG_PHOTO to photo
                    )
                }
    }

    private var callback: Callback? = null
    private var viewBinding: FragmentPhotoBinding? = null
    private val binding get() = viewBinding!!
    private lateinit var photo: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photo = arguments?.getParcelable(ARG_PHOTO) ?: throw IllegalArgumentException()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentPhotoBinding.inflate(inflater, container, false)

        with(binding) {
            if (photo.likedByUser) detailPhotoLikeImage.setImageResource(R.drawable.ic_like)
            else detailPhotoLikeImage.setImageResource(R.drawable.ic_empty_like)

            detailPhotoNumberOfLikes.text = "${photo.likes}"

            detailPhotoDescription.text = photo.description

            detailPhotoButtonOpenWebView.setOnClickListener {
                callback?.loadUrl(Uri.parse(photo.links.html))
            }

            startDownloading()
            Picasso.get()
                    .load(photo.urls.regular)
                    .into(detailPhotoImage, object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            detailPhotoProgressBar.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            endDownloading()
                            Toast.makeText(getViewContext(), R.string.connection_error, Toast.LENGTH_SHORT)
                                    .show()
                            e?.printStackTrace()
                        }
                    })
        }

        return binding.root
    }

    // NOTE: view implementation

    override fun startDownloading() {
        //  detailPhotoProgressBar.visibility = View.VISIBLE
    }

    override fun endDownloading() {
        // detailPhotoProgressBar.visibility = View.GONE
    }

    override fun getViewContext() = context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as Callback
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}