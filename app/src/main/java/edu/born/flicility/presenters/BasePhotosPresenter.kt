package edu.born.flicility.presenters

import edu.born.flicility.views.BaseView

interface BasePhotosPresenter<BV : BaseView>: SubscribePresenter<BV> {
    var query: Query
    fun getPhotos() // next portion of photos
}