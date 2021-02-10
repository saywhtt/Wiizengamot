package edu.born.flicility.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import edu.born.flicility.R
import edu.born.flicility.model.Photo
import edu.born.flicility.views.PhotoView
import java.lang.Exception

class PhotoFragment : VisibleFragment(), PhotoView {

    companion object {
        private const val ARG_PHOTO = "ARG_PHOTO"
        fun newInstance(photo: Photo): PhotoFragment {
            val photoFragment = PhotoFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_PHOTO, photo)
            photoFragment.arguments = bundle
            return photoFragment
        }
    }

    private lateinit var photo: Photo
    private lateinit var photoImageView: ImageView
    private lateinit var likeImageView: ImageView
    private lateinit var likesTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photo = arguments?.getParcelable(ARG_PHOTO) ?: throw IllegalArgumentException()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        photoImageView = view.findViewById(R.id.fragment_photo_image_view)
        likeImageView = view.findViewById(R.id.fragment_photo_like_image_view)
        likesTextView = view.findViewById(R.id.fragment_photo_likes_text_view)
        descriptionTextView = view.findViewById(R.id.fragment_photo_description)
        progressBar = view.findViewById(R.id.fragment_photo_progress_bar)

        if (photo.likedByUser) likeImageView.setImageResource(R.drawable.ic_like)
        else likeImageView.setImageResource(R.drawable.ic_empty_like)

        likesTextView.text = "${photo.likes}"

        descriptionTextView.text = photo.description

        startDownloading()
        Picasso.get()
                .load(photo.urls.regular)
                .into(photoImageView, object : Callback {
                    override fun onSuccess() {
                        progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        endDownloading()
                        Toast.makeText(getViewContext(), R.string.connection_error, Toast.LENGTH_SHORT)
                                .show()
                        e?.printStackTrace()
                    }
                })

        return view
    }

    // NOTE: view implementation

    override fun startDownloading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun endDownloading() {
        progressBar.visibility = View.GONE
    }

    override fun getViewContext() = context
}