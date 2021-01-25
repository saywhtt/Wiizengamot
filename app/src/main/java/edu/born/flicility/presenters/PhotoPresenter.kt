package edu.born.flicility.presenters

import android.widget.ImageView
import edu.born.flicility.DownloadState

interface PhotoPresenter {
    fun getImage(imageView: ImageView, url: String, state: DownloadState)
    fun destroyDownloadQueue()
}