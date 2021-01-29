package edu.born.flicility.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import edu.born.flicility.R
import edu.born.flicility.model.Photo
import edu.born.flicility.views.PhotoView

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
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photo = arguments?.getParcelable(ARG_PHOTO) ?: throw IllegalArgumentException()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        imageView = view.findViewById(R.id.imageView)

        Picasso.get()
                .load(photo.urls.thumb)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageView)

        return view
    }

    override fun getViewContext() = context
}