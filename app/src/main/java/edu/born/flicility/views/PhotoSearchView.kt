package edu.born.flicility.views

import edu.born.flicility.model.Photo

interface PhotoSearchView: BaseView {
    fun startDownloading()
    fun endDownloading()
    fun update(data: List<Photo>)
    fun hideKeyboard()
}