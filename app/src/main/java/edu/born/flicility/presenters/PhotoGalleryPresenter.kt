package edu.born.flicility.presenters

interface PhotoGalleryPresenter {
    fun getPhotos()
    fun getPhotosByNewQuery(query: String)
    fun getNextPageByCurrentQuery()
}