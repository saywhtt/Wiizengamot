package edu.born.flicility.presenters

import android.widget.ImageView

interface PhotoPresenter {
    fun bindImage(imageView: ImageView, url: String, state: DownloadState)
    fun destroyDownloadQueue()
}