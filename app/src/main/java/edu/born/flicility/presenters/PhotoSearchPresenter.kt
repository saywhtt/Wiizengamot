package edu.born.flicility.presenters

interface PhotoSearchPresenter {
    fun getPhotosByNewQuery(text: String)
    fun getNextPhotosByCurrentQuery()
}