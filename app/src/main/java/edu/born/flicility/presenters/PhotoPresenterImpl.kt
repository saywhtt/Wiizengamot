package edu.born.flicility.presenters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import edu.born.flicility.DownloadState
import edu.born.flicility.DownloadState.QUEUE
import edu.born.flicility.DownloadState.SINGLE
import edu.born.flicility.FlickrFetchr
import edu.born.flicility.R
import edu.born.flicility.adapters.PhotoAdapter
import edu.born.flicility.network.PhotoService
import edu.born.flicility.views.PhotoView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

private val TAG = PhotoPresenterImpl::class.java.simpleName

class PhotoPresenterImpl(private val service: PhotoService) : BasePresenter<PhotoView>(), PhotoPresenter {

    private val map = ConcurrentHashMap<ImageView, String>()
    private val queue = LinkedBlockingQueue<ImageView>()
    private val mainHandler = Handler(Looper.getMainLooper())

    private val customer = Thread {
        while (true) {
            val image = queue.take()
            var url: String?
            synchronized(map) {
                url = map[image]
            }
            map.remove(image)
            url?.let {
                downloadImage(image, it)
            }
        }
    }

    init {
        customer.start()
    }

    override fun bindImage(imageView: ImageView, url: String, state: DownloadState) {
        when (state) {
            SINGLE -> {
                downloadImage(imageView, url)
            }
            QUEUE -> {
                synchronized(map) {
                    if (!queue.contains(imageView)) {
                        queue.add(imageView)
                    }
                    map[imageView] = url
                }
            }
        }
    }

    private fun downloadImage(imageView: ImageView, url: String) {
        val bitmapBytes = service.getImage(url).execute().body()?.bytes()
        var bitmap: Bitmap? = null
        if (bitmapBytes != null) {
            bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.size)
        }
        mainHandler.post {
            imageView.setImageBitmap(bitmap)
        }
    }

    override fun destroyDownloadQueue() {
        customer.interrupt()
    }
}