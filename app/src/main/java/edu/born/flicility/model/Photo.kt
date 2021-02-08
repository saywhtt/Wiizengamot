package edu.born.flicility.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(@SerializedName("id") var id: String,
                 @SerializedName("description") var description: String?,
                 @SerializedName("width") var width: Int,
                 @SerializedName("height") var height: Int,
                 @SerializedName("urls") var urls: Urls) : Parcelable