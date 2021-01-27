package edu.born.flicility.presenters

import android.widget.Toast
import edu.born.flicility.R
import edu.born.flicility.network.PhotoSearchResponse
import edu.born.flicility.network.PhotoService
import edu.born.flicility.views.PhotoSearchView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val TAG = PhotoSearchPresenterImpl::class.java.simpleName

class PhotoSearchPresenterImpl(private val service: PhotoService) : BasePresenter<PhotoSearchView>(), PhotoSearchPresenter {
    private val query = Query.Search()

    override fun getPhotosByNewQuery(text: String) {
        query.text = text
        query.currentPage = 0
        query.isNoMoreResults = false

        query.text?.let {
            getByQuery(it)
        }
    }

    override fun getNextPhotosByCurrentQuery() {
        if (this.query.isNoMoreResults) return
        query.text?.let {
            getByQuery(it)
        }
    }

    private fun getByQuery(text: String) {
        view?.startDownloading()
        service.getPhotosByQuery(page = ++query.currentPage, query = text)
                .enqueue(object : Callback<PhotoSearchResponse> {
                    override fun onResponse(call: Call<PhotoSearchResponse>, response: Response<PhotoSearchResponse>) {
                        response.body()?.let {
                            if (it.results.isEmpty()) this@PhotoSearchPresenterImpl.query.isNoMoreResults = true
                            else view?.update(it.results)
                            view?.endDownloading()
                        }
                    }

                    override fun onFailure(call: Call<PhotoSearchResponse>, t: Throwable) {
                        view?.endDownloading()
                        Toast.makeText(view?.getViewContext(), R.string.connection_error, Toast.LENGTH_SHORT)
                                .show()
                        t.printStackTrace()
                    }
                })
    }
}