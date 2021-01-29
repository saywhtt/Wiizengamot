package edu.born.flicility.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import edu.born.flicility.R
import edu.born.flicility.model.Photo
import edu.born.flicility.presenters.PhotoPresenter

class PhotoAdapter(private val photoPresenter: PhotoPresenter) :
        RecyclerView.Adapter<PhotoAdapter.PhotoHolder>(), BaseAdapter<Photo> {

    var onBottomReachedListener: OnBottomReachedListener? = null
    var onPhotoClickedListener: OnPhotoClickedListener? = null

    private val data: MutableList<Photo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder =
            PhotoHolder(view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        if (itemCount - 1 == position) onBottomReachedListener?.onBottomReached()
        holder.bind(data[position])
        Picasso.get()
                .load(data[position].urls.thumb)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imageView)
        //photoPresenter.bindImage(holder.imageView, data[position].urls.thumb, DownloadState.QUEUE)
    }

    override fun insertAll(items: List<Photo>) {
        val wasNoDataBefore = data.isEmpty()
        data.addAll(items)
        if (wasNoDataBefore) notifyDataSetChanged()
    }

    override fun deleteAll() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class PhotoHolder(val view: View,
                            val imageView: ImageView = view.findViewById(R.id.item_image_view)) : ViewHolder(view), View.OnClickListener {
        private var photo: Photo? = null

        init {
            view.setOnClickListener(this)
        }

        fun bind(photo: Photo) {
            this.photo = photo
        }

        override fun onClick(view: View) {
            val position = data.indexOf(photo)
            val photos = data as ArrayList<Photo>
            onPhotoClickedListener?.onPhotoClicked(position, photos)
        }
    }
}