package edu.born.flicility.presenters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class Query(open var currentPage: Int = 0,
                   open var isNoMoreResults: Boolean = false) : Parcelable {
    @Parcelize
    class All(override var currentPage: Int = 0,
              override var isNoMoreResults: Boolean = false) : Query(currentPage, isNoMoreResults)

    @Parcelize
    class Search(var text: String? = null,
                 override var currentPage: Int = 0,
                 override var isNoMoreResults: Boolean = false) : Query(currentPage, isNoMoreResults)
}