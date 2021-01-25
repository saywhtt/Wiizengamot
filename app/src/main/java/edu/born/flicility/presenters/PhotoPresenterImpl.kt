package edu.born.flicility.presenters

import android.graphics.BitmapFactory
import android.widget.ImageView
import edu.born.flicility.DownloadState
import edu.born.flicility.DownloadState.*
import edu.born.flicility.network.PhotoService
import edu.born.flicility.views.PhotoView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.LinkedBlockingQueue

private val TAG = PhotoPresenterImpl::class.java.simpleName

class PhotoPresenterImpl(private val service: PhotoService) : BasePresenter<PhotoView>(), PhotoPresenter {

    private val map = ConcurrentHashMap<ImageView, String>()
    private val queue = LinkedBlockingQueue<ImageView>()

    private val customer = Thread {
        while (true) {
            val image = queue.take()
            val url = map[image]
            map.remove(image)
            if (url != null) downloadImage(image, url)
        }
    }

    init {
        customer.start()
    }

    override fun getImage(imageView: ImageView, url: String, state: DownloadState) {
        when (state) {
            SINGLE -> {
                downloadImage(imageView, url)
            }
            QUEUE -> {
                if (!queue.contains(imageView))
                    queue.add(imageView)
                map[imageView] = url
            }
        }
    }

    private fun downloadImage(imageView: ImageView, url: String) {
        service.getImage(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val bitmapBytes = it?.bytes()
                    if (bitmapBytes != null) {
                        val bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.size)
                        imageView.setImageBitmap(bitmap)
                    }
                }
    }
}