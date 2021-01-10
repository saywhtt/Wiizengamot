package edu.born.flicility.model

import com.google.gson.annotations.SerializedName

data class Photo(@SerializedName("title") var title: String,
                 @SerializedName("id") var id: String,
                 @SerializedName("urls") var urls: Urls)