package edu.born.flicility.network

import com.google.gson.annotations.SerializedName
import edu.born.flicility.model.Photo

data class PhotoSearchResponse(@SerializedName("results") val results: List<Photo>)