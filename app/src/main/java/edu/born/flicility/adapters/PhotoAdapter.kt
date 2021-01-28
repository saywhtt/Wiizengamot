package edu.born.flicility.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import edu.born.flicility.DownloadState
import edu.born.flicility.PhotoDownloader
import edu.born.flicility.R
import edu.born.flicility.model.Photo
import edu.born.flicility.presenters.PhotoPresenter

class PhotoAdapter(private val photoPresenter: PhotoPresenter,
                   private val photoDownloader: PhotoDownloader) :
        RecyclerView.Adapter<PhotoAdapter.PhotoHolder>(), BaseAdapter<Photo> {

    var onBottomReachedListener: OnBottomReachedListener? = null

    private val data: MutableList<Photo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder =
            PhotoHolder(itemView = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        if (itemCount - 1 == position) onBottomReachedListener?.onBottomReached()

        val context = holder.imageView.context
        val defaultImage = R.drawable.ic_launcher_foreground
        holder.bind(ContextCompat.getDrawable(context, defaultImage))
        photoPresenter.bindImage(holder.imageView, data[position].urls.thumb, DownloadState.QUEUE)
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

    class PhotoHolder(itemView: View) : ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image_view)

        fun bind(drawable: Drawable?) {
            imageView.setImageDrawable(drawable)
        }
    }
}