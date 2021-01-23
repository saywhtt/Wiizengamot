package edu.born.flicility.network

import edu.born.flicility.model.Photo
import edu.born.flicility.network.API.AUTHORIZATION_HEADER
import edu.born.flicility.network.API.PER_PAGE
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

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

    @GET
    fun getImage(@Url url: String): Observable<ResponseBody>
}