package edu.born.flicility.presenters

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Query(var text: String? = null,
                 var currentPage: Int = 0,
                 var isNoMoreResults: Boolean = false,
                 val type: QueryType) : Parcelable