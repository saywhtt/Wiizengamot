package edu.born.flicility.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(@SerializedName("title") var title: String,
                 @SerializedName("id") var id: String,
                 @SerializedName("urls") var urls: Urls) : Parcelable