package edu.born.flicility.presenters

interface PhotoListPresenter {
    fun getPhotos()
    fun getPhotosByNewQuery(query: String)
    fun getNextPageByCurrentQuery()
}