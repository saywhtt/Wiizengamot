package edu.born.flicility.activities

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
        private const val EXTRA_PHOTO_LIST = "PHOTO_LIST"
        private const val EXTRA_PHOTO_POSITION = "PHOTO_POSITION"
        private const val EXTRA_QUERY = "QUERY"
        fun newIntent(context: Context?, position: Int, photos: ArrayList<Photo>, query: Query) =
                Intent(context, PhotoPagerActivity::class.java).putParcelableArrayListExtra(EXTRA_PHOTO_LIST, photos)
                        .putExtra(EXTRA_PHOTO_POSITION, position)
                        .putExtra(EXTRA_QUERY, query)
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

        photos = intent.getParcelableArrayListExtra<Photo>(EXTRA_PHOTO_LIST) as MutableList<Photo>
        query = intent.getParcelableExtra(EXTRA_QUERY) ?: throw IllegalArgumentException()

        subscribeToPresenter()

        when (query) {
            is Query.All -> photoListPresenter.setQuery(query as Query.All)
            is Query.Search -> photoSearchPresenter.setQuery(query as Query.Search)
        }

        viewPager = findViewById(R.id.photo_view_pager)
        viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): PhotoFragment {
                if (photos.size - 1 == position) {
                    when (query) {
                        is Query.All -> photoListPresenter.getPhotos()
                        is Query.Search -> photoSearchPresenter.getNextPhotosByCurrentQuery()
                    }
                }
                return PhotoFragment.newInstance(photos[position])
            }

            override fun getCount() = photos.size

            override fun setPrimaryItem(container: ViewGroup, position: Int, any: Any) {
                super.setPrimaryItem(container, position, any)
                currentItemPosition = position
            }
        }
        viewPager.currentItem = intent.getIntExtra(EXTRA_PHOTO_POSITION, 0)
    }

    private fun subscribeToPresenter() {
        when (query) {
            is Query.All -> (photoListPresenter as BasePresenter<PhotoListView>).subscribe(this)
            is Query.Search -> (photoListPresenter as BasePresenter<PhotoListView>).subscribe(this)
        }
    }

    private fun unsubscribeFromPresenter() {
        when (query) {
            is Query.All -> (photoListPresenter as BasePresenter<PhotoListView>).unsubscribe()
            is Query.Search -> (photoListPresenter as BasePresenter<PhotoListView>).unsubscribe()
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