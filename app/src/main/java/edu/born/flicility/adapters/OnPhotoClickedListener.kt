package edu.born.flicility.adapters

import edu.born.flicility.model.Photo

interface OnPhotoClickedListener {
    fun onPhotoClicked(position: Int, photos: ArrayList<Photo>)
}