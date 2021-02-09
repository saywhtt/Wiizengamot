package edu.born.flicility.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
    private val set = ConstraintSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder =
            PhotoHolder(view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {

        if (itemCount - 1 == position) onBottomReachedListener?.onBottomReached()

        with(data[position]) {

            holder.bind(this)

            Picasso.get()
                    .load(urls.regular)
                    //.placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.imageView)

            with(set) {
                val ratio = String.format("%d:%d", width, height)
                clone(holder.constraintLayout)
                setDimensionRatio(holder.imageView.id, ratio)
                applyTo(holder.constraintLayout)
            }
        }

    }

    override fun insertAll(items: List<Photo>) {
        val wasNoDataBefore = data.isEmpty()
        val dataSizeBefore = data.size
        data.addAll(items)
        if (wasNoDataBefore) notifyItemRangeInserted(0, items.size)
        else notifyItemRangeInserted(dataSizeBefore, items.size)
    }

    override fun deleteAll() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class PhotoHolder(view: View) : ViewHolder(view), View.OnClickListener {

        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.item_constraint_layout)
        val imageView: ImageView = view.findViewById(R.id.item_image_view)

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