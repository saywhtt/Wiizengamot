package edu.born.flicility.service

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.app.JobIntentService
import edu.born.flicility.app.App
import edu.born.flicility.getLastResultId
import edu.born.flicility.model.Photo
import edu.born.flicility.network.PhotoService
import edu.born.flicility.setLastResultId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

private val TAG: String get() = PollService::class.java.simpleName

const val JOB_ID = 1000

fun enqueueWork(context: Context, work: Intent) {
    JobIntentService.enqueueWork(context, PollService::class.java, JOB_ID, work)
}

class PollService : JobIntentService() {

    @Inject
    lateinit var photoService: PhotoService

    override fun onCreate() {
        super.onCreate()
        (applicationContext as App).appComponent.inject(this)
    }

    override fun onHandleWork(intent: Intent) {
        if (isNetworkAvailableAndConnected()) {
            val lastResultId = getLastResultId(this)
            photoService.getPhotos(page = "1", per_page = 1)
                    .enqueue(object : Callback<List<Photo>> {
                        override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                            response.body()?.let {
                                if (it.isEmpty()) return
                                else {
                                    val resultId = it[0].id
                                    if (resultId != lastResultId) {
                                        Log.d(TAG, "Available new results!")
                                        setLastResultId(this@PollService, resultId)
                                    } else {
                                        Log.d(TAG, "Not Available new results.")
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<List<Photo>>, t: Throwable) = t.printStackTrace()
                    })
        }
    }

    fun isNetworkAvailableAndConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                ?: false
    }

}
