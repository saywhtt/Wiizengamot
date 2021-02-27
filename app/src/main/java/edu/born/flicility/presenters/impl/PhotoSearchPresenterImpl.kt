package edu.born.flicility.presenters.impl

import android.widget.Toast
import edu.born.flicility.R
import edu.born.flicility.network.PhotoService
import edu.born.flicility.presenters.PhotoSearchPresenter
import edu.born.flicility.presenters.Query
import edu.born.flicility.presenters.QueryType
import edu.born.flicility.views.PhotoListView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class PhotoSearchPresenterImpl(private val service: PhotoService) : PhotoSearchPresenter<PhotoListView> {

    override var query = Query(type = QueryType.SEARCH)
    override var view: PhotoListView? = null

    override fun getPhotosByNewQuery(text: String) {
        query.text = text
        query.currentPage = 0
        query.isNoMoreResults = false

        query.text?.let {
            getByQuery(it)
        }
    }

    override fun getPhotos() {
        if (this.query.isNoMoreResults) return
        query.text?.let {
            getByQuery(it)
        }
    }

    private fun getByQuery(text: String) {
        view?.startDownloading()
        service.getPhotosByQuery(page = ++query.currentPage, query = text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.results.isEmpty()) {
                        query.isNoMoreResults = true
                    } else {
                        view?.update(it.results)
                    }
                    view?.endDownloading()
                }, {
                    view?.endDownloading()
                    Toast.makeText(view?.getViewContext(), R.string.connection_error, Toast.LENGTH_SHORT)
                            .show()
                    it.printStackTrace()
                })
    }

    override fun subscribe(view: PhotoListView) {
        this.view = view
    }

    override fun unsubscribe() {
        this.view = null
    }
}