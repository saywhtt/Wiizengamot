package edu.born.flicility.presenters

import edu.born.flicility.views.BaseView

interface PhotoSearchPresenter<BV : BaseView>: BasePhotosPresenter<BV> {
    fun getPhotosByNewQuery(text: String)
}