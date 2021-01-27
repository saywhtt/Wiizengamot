package edu.born.flicility.presenters

import android.widget.ImageView
import edu.born.flicility.DownloadState

interface PhotoPresenter {
    fun bindImage(imageView: ImageView, url: String, state: DownloadState)
    fun destroyDownloadQueue()
}