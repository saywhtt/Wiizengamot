package edu.born.flicility.views

import edu.born.flicility.model.Photo

interface PhotoGalleryView: BaseView {
    fun hideKeyboard()
    fun updateData(data: List<Photo>)
    fun startDownloading()
    fun endDownloading()
}