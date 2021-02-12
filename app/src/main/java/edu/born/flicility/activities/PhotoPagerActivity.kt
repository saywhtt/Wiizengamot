package edu.born.flicility.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import edu.born.flicility.R
import edu.born.flicility.app.App
import edu.born.flicility.fragments.PhotoFragment
import edu.born.flicility.model.Photo
import edu.born.flicility.presenters.BasePresenter
import edu.born.flicility.presenters.PhotoListPresenter
import edu.born.flicility.presenters.PhotoSearchPresenter
import edu.born.flicility.presenters.Query
import edu.born.flicility.views.PhotoListView
import javax.inject.Inject
import javax.inject.Named

class PhotoPagerActivity : AppCompatActivity(), PhotoListView {

    companion object {
        private const val EXTRA_PHOTOS = "PHOTOS"
        private const val EXTRA_PHOTO_POSITION = "PHOTO_POSITION"
        private const val EXTRA_QUERY = "QUERY"
        fun newIntent(context: Context?, position: Int, photos: ArrayList<Photo>, query: Query) =
                Intent(context, PhotoPagerActivity::class.java).putParcelableArrayListExtra(EXTRA_PHOTOS, photos)
                        .putExtra(EXTRA_PHOTO_POSITION, position)
                        .putExtra(EXTRA_QUERY, query)

        fun getPhotos(result: Intent) = result.getParcelableArrayListExtra<Photo>(EXTRA_PHOTOS) as MutableList<Photo>
        fun getPosition(result: Intent) = result.getIntExtra(EXTRA_PHOTO_POSITION, 0)
        fun getQuery(result: Intent) = result.getParcelableExtra<Query>(EXTRA_QUERY)
    }

    @Inject
    @Named("photoPagerActivity")
    lateinit var photoListPresenter: PhotoListPresenter

    @Inject
    lateinit var photoSearchPresenter: PhotoSearchPresenter

    private lateinit var viewPager: ViewPager
    private var photos = mutableListOf<Photo>()
    private var photosDownloadedByPager = mutableListOf<Photo>()
    private var currentItemPosition = 0
    private lateinit var query: Query

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_pager)

        val app = applicationContext as App
        app.plusPhotoComponent().inject(this)

        photos = intent.getParcelableArrayListExtra<Photo>(EXTRA_PHOTOS) as MutableList<Photo>
        query = intent.getParcelableExtra(EXTRA_QUERY) ?: throw IllegalArgumentException()

        subscribeToPresenter()

        executeByQuery({
            photoListPresenter.setQuery(query as Query.All)
        }, {
            photoSearchPresenter.setQuery(query as Query.Search)
        })

        viewPager = findViewById(R.id.photo_view_pager)
        viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): PhotoFragment {
                if (photos.size - 1 == position) {
                    executeByQuery({
                        photoListPresenter.getPhotos()
                    }, {
                        photoSearchPresenter.getNextPhotosByCurrentQuery()
                    })
                }
                return PhotoFragment.newInstance(photos[position])
            }

            override fun getCount() = photos.size

            override fun setPrimaryItem(container: ViewGroup, position: Int, any: Any) {
                super.setPrimaryItem(container, position, any)
                currentItemPosition = position
                setPreparedResult(currentItemPosition)
            }
        }
        viewPager.currentItem = intent.getIntExtra(EXTRA_PHOTO_POSITION, 0)
    }

    private fun subscribeToPresenter() =
            executeByQuery({
                (photoListPresenter as BasePresenter<PhotoListView>).subscribe(this)
            }, {
                (photoListPresenter as BasePresenter<PhotoListView>).subscribe(this)
            })

    private fun unsubscribeFromPresenter() =
            executeByQuery({
                (photoListPresenter as BasePresenter<PhotoListView>).unsubscribe()
            }, {
                (photoListPresenter as BasePresenter<PhotoListView>).unsubscribe()
            })

    private fun setPreparedResult(position: Int) {
        val intent = Intent().putParcelableArrayListExtra(EXTRA_PHOTOS, photos as ArrayList<Photo>)
                .putExtra(EXTRA_PHOTO_POSITION, position)
                .putExtra(EXTRA_QUERY, query)
        setResult(Activity.RESULT_OK, intent)
    }

    private fun executeByQuery(byAll: () -> Unit, bySearch: () -> Unit) {
        when (query) {
            is Query.All -> byAll.invoke()
            is Query.Search -> bySearch.invoke()
        }
    }

    // NOTE: view implementation

    override fun update(data: List<Photo>) {
        photos.addAll(data)
        photosDownloadedByPager.addAll(data)
        viewPager.adapter?.notifyDataSetChanged()
    }

    override fun startDownloading() {}

    override fun endDownloading() {}

    override fun getViewContext() = this

    // NOTE: life cycle methods

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeFromPresenter()
    }
}