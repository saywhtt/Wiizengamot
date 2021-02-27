package edu.born.flicility.presenters.impl

import android.widget.Toast
import edu.born.flicility.R
import edu.born.flicility.network.PhotoService
import edu.born.flicility.presenters.PhotoListPresenter
import edu.born.flicility.presenters.Query
import edu.born.flicility.presenters.QueryType
import edu.born.flicility.views.PhotoListView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class PhotoListPresenterImpl(private val service: PhotoService) : PhotoListPresenter<PhotoListView> {

    override var query = Query(type = QueryType.ALL)
    override var view: PhotoListView? = null

    override fun getPhotos() {
        if (query.isNoMoreResults) return
        view?.startDownloading()
        service.getPhotos(page = ++query.currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isEmpty()) {
                        query.isNoMoreResults = true
                    } else {
                        view?.update(it)
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