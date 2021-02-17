package edu.born.flicility.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Links(@SerializedName("html") var html: String) : Parcelable