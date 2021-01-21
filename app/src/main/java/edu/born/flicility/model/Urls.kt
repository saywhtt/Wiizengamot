package edu.born.flicility.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Urls(@SerializedName("regular") var regular: String): Parcelable