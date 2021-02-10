package edu.born.flicility.presenters

interface PhotoSearchPresenter {
    fun getPhotosByNewQuery(text: String)
    fun getNextPhotosByCurrentQuery()
    fun getQuery(): Query.Search
    fun setQuery(query: Query.Search)
}