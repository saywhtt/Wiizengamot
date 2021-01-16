package edu.born.flicility.network

import edu.born.flicility.model.Photo
import edu.born.flicility.network.API.AUTHORIZATION_HEADER
import edu.born.flicility.network.API.PER_PAGE
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PhotoService {
    @GET("photos")
    fun getPhotos(@Query("page") page: Int,
                  @Query("per_page") per_page: Int = PER_PAGE,
                  @Header("Authorization") auth: String = AUTHORIZATION_HEADER): Call<List<Photo>>

    @GET("search/photos")
    fun getPhotosByQuery(@Query("query") query: String,
                         @Query("page") page: Int,
                         @Query("per_page") per_page: Int = PER_PAGE,
                         @Header("Authorization") auth: String = AUTHORIZATION_HEADER): Call<PhotoSearchResponse>
}