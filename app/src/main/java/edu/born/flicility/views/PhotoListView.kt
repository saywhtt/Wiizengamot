package edu.born.flicility.views

import edu.born.flicility.model.Photo

interface PhotoListView: BaseView {
    fun hideKeyboard()
    fun updateData(data: List<Photo>)
    fun startDownloading()
    fun endDownloading()
}