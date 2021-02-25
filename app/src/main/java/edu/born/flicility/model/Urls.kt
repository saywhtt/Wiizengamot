package edu.born.flicility.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Urls(@SerializedName("raw") var raw: String,
                @SerializedName("full") var full: String,
                @SerializedName("regular") var regular: String,
                @SerializedName("small") var small: String,
                @SerializedName("thumb") var thumb: String) : Parcelable