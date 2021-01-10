package edu.born.flicility.presenters

import android.util.Log
import edu.born.flicility.fragments.PhotoGalleryFragment
import edu.born.flicility.model.Photo
import edu.born.flicility.network.PhotoSearchResponse
import edu.born.flicility.network.PhotoService
import edu.born.flicility.presenters.PhotoGalleryPresenterImpl.Query.*
import edu.born.flicility.views.PhotoGalleryView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoGalleryPresenterImpl(private val service: PhotoService) : BasePresenter<PhotoGalleryView>(), PhotoGalleryPresenter {

    private val TAG = PhotoGalleryPresenterImpl::class.java.canonicalName

    sealed class Query(var currentPage: Int = 0,
                       var isNoMoreResults: Boolean = false) {
        class All(currentPage: Int = 0,
                  isNoMoreResults: Boolean = false) : Query(currentPage, isNoMoreResults)

        class Search(var query: String?,
                     currentPage: Int = 0,
                     isNoMoreResults: Boolean = false) : Query(currentPage, isNoMoreResults)
    }

    private val all = All()
    private val search = Search(query = null)

    var currentQuery: Query = all

    override fun getNextPageByCurrentQuery() {
        when (currentQuery) {
            is All -> getPhotos()
            is Search -> {
                getPhotosByQuery((currentQuery as Search).query)
            }
        }
    }

    override fun getPhotosByNewQuery(query: String) {
        search.query = query
        search.currentPage = 0
        search.isNoMoreResults = false

        currentQuery = search
        getPhotosByQuery(search.query)
    }

    private fun getPhotosByQuery(query: String?) {
        if (currentQuery.isNoMoreResults) return
        query?.let {
            view?.startDownloading()
            service.getPhotosByQuery(page = "${++currentQuery.currentPage}", query = query)
                    .enqueue(object : Callback<PhotoSearchResponse> {

                        override fun onResponse(call: Call<PhotoSearchResponse>, response: Response<PhotoSearchResponse>) {
                            response.body()?.let {
                                if (it.results.isEmpty()) currentQuery.isNoMoreResults = true
                                else view?.updateData(it.results)
                                view?.endDownloading()
                            }
                        }

                        override fun onFailure(call: Call<PhotoSearchResponse>, t: Throwable) = t.printStackTrace()
                    })
        }
    }

    override fun getPhotos() {
        when (currentQuery) {
            is Search -> {
                search.query = null
                search.currentPage = 0
                currentQuery = all
            }
            is All -> {
                if (all.isNoMoreResults) return
            }
        }
        view?.startDownloading()
        service.getPhotos(page = "${++all.currentPage}")
                .enqueue(object : Callback<List<Photo>> {

                    override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                        response.body()?.let {
                            if (it.isEmpty()) currentQuery.isNoMoreResults = true
                            else view?.updateData(it)
                            Log.d(PhotoGalleryFragment.TAG, "onResponse ${it.size}")
                            view?.endDownloading()
                        }
                    }

                    override fun onFailure(call: Call<List<Photo>>, t: Throwable) = t.printStackTrace()
                })
    }

}