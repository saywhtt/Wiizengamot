package edu.born.flicility.presenters

sealed class Query(var currentPage: Int = 0,
                   var isNoMoreResults: Boolean = false) {
    class All(currentPage: Int = 0,
              isNoMoreResults: Boolean = false) : Query(currentPage, isNoMoreResults)

    class Search(var text: String? = null,
                 currentPage: Int = 0,
                 isNoMoreResults: Boolean = false) : Query(currentPage, isNoMoreResults)
}