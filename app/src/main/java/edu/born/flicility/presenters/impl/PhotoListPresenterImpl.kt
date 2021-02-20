package edu.born.flicility.presenters.impl

import android.widget.Toast
import edu.born.flicility.R
import edu.born.flicility.model.Photo
import edu.born.flicility.network.PhotoService
import edu.born.flicility.presenters.PhotoListPresenter
import edu.born.flicility.presenters.Query
import edu.born.flicility.presenters.QueryType
import edu.born.flicility.views.PhotoListView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val TAG = PhotoListPresenterImpl::class.java.simpleName

class PhotoListPresenterImpl(private val service: PhotoService) : PhotoListPresenter<PhotoListView> {

    override var query = Query(type = QueryType.ALL)
    override var view: PhotoListView? = null

    override fun getPhotos() {
        if (query.isNoMoreResults) return
        view?.startDownloading()
        service.getPhotos(page = ++query.currentPage)
                .enqueue(object : Callback<List<Photo>> {
                    override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                        response.body()?.let {
                            if (it.isEmpty()) query.isNoMoreResults = true
                            else view?.update(it)
                            view?.endDownloading()
                        }
                    }

                    override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                        view?.endDownloading()
                        Toast.makeText(view?.getViewContext(), R.string.connection_error, Toast.LENGTH_SHORT)
                                .show()
                        t.printStackTrace()
                    }
                })
    }

    override fun subscribe(view: PhotoListView) {
        this.view = view
    }

    override fun unsubscribe() {
        this.view = null
    }
}