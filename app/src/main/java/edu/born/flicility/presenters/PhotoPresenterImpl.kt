package edu.born.flicility.presenters

import android.graphics.BitmapFactory
import android.widget.ImageView
import edu.born.flicility.network.PhotoService
import edu.born.flicility.views.PhotoView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.ConcurrentHashMap

private val TAG = PhotoPresenterImpl::class.java.simpleName

class PhotoPresenterImpl(private val service: PhotoService) : BasePresenter<PhotoView>(), PhotoPresenter {
    override fun getImage(imageView: ImageView, url: String) {
        service.getImage(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val bitmapBytes = it?.bytes()
                    bitmapBytes?.let { array ->
                        val bitmap = BitmapFactory.decodeByteArray(array, 0, array.size)
                        imageView.setImageBitmap(bitmap)
                    }
                }
    }
}