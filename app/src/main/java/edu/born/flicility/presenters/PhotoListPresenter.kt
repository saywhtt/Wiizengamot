package edu.born.flicility.presenters

interface PhotoListPresenter {
    fun getPhotos()
    fun getQuery(): Query.All
    fun setQuery(query: Query.All)
}