package edu.born.flicility.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
    private lateinit var photoImageView: ImageView
    private lateinit var likeImageView: ImageView
    private lateinit var likesTextView: TextView
    private lateinit var descriptionTextView: TextView

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

        if (photo.likedByUser) likeImageView.setImageResource(R.drawable.ic_like)
        else likeImageView.setImageResource(R.drawable.ic_empty_like)

        likesTextView.text = "${photo.likes}"

        descriptionTextView.text = photo.description

        Picasso.get()
                .load(photo.urls.regular)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(photoImageView)

        return view
    }

    override fun getViewContext() = context
}