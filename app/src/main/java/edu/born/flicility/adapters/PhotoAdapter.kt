package edu.born.flicility.adapters

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

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoHolder>(), BaseAdapter<Photo> {

    private lateinit var recyclerView: RecyclerView

    var onBottomReachedListener: OnBottomReachedListener? = null
    var onPhotoClickedListener: OnPhotoClickedListener? = null

    private var data: MutableList<Photo> = mutableListOf()
    private val photoHolderConstraintSet = ConstraintSet()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder =
            PhotoHolder(view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {

        if (position == (itemCount - 1)) onBottomReachedListener?.onBottomReached()

        with(data[position]) {

            holder.bind(this)

            Picasso.get()
                    .load(urls.regular)
                    .into(holder.imageView)

            with(photoHolderConstraintSet) {
                val ratio = String.format("%d:%d", width, height)
                clone(holder.constraintLayout)
                setDimensionRatio(holder.imageView.id, ratio)
                applyTo(holder.constraintLayout)
            }
        }
    }

    override fun update(items: List<Photo>) {
        val wasNoDataBefore = data.isEmpty()
        val dataSizeBefore = data.size

        data.addAll(items)

        if (wasNoDataBefore) {
            notifyItemRangeInserted(0, items.size)
        } else {
            notifyItemRangeInserted(dataSizeBefore, items.size)
        }
    }

    override fun updateWithStartPosition(items: List<Photo>, position: Int) {
        data.addAll(items)
        recyclerView.layoutManager?.scrollToPosition(position)
    }

    override fun deleteAll() {
        data.clear()
        notifyDataSetChanged()
    }

    inner class PhotoHolder(view: View) : ViewHolder(view), View.OnClickListener {

        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.photo_item_cl)
        val imageView: ImageView = view.findViewById(R.id.photo_item_iv)

        private lateinit var photo: Photo

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