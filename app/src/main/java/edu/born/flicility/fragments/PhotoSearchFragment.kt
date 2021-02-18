package edu.born.flicility.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.SearchView
import edu.born.flicility.R
import edu.born.flicility.activities.PhotoPagerActivity
import edu.born.flicility.adapters.OnBottomReachedListener
import edu.born.flicility.adapters.OnPhotoClickedListener
import edu.born.flicility.adapters.PhotoAdapter
import edu.born.flicility.fragments.abstraction.AbstractPhotoListFragment
import edu.born.flicility.model.Photo
import edu.born.flicility.presenters.BasePresenter
import edu.born.flicility.presenters.PhotoPresenter
import edu.born.flicility.presenters.PhotoSearchPresenter
import edu.born.flicility.views.PhotoListView
import javax.inject.Inject

class PhotoSearchFragment : AbstractPhotoListFragment() {

    override lateinit var adapter: PhotoAdapter

    @Inject
    lateinit var photoSearchPresenter: PhotoSearchPresenter

    @Inject
    lateinit var photoPresenter: PhotoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app.plusPhotoComponent().inject(this)
        adapter = PhotoAdapter()
        subscribeToPresenter()
    }

    override fun getPreparedAdapter(): PhotoAdapter {
        adapter.onBottomReachedListener = object : OnBottomReachedListener {
            override fun onBottomReached() {
                photoSearchPresenter.getNextPhotosByCurrentQuery()
            }
        }
        adapter.onPhotoClickedListener = object : OnPhotoClickedListener {
            override fun onPhotoClicked(position: Int, photos: ArrayList<Photo>) {
                val intent = PhotoPagerActivity.newIntent(context, position, photos, photoSearchPresenter.getQuery())
                startActivity(intent)
            }
        }
        return adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_search, menu)

        val searchView = menu.findItem(R.id.menu_item_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                adapter.deleteAll()
                photoSearchPresenter.getPhotosByNewQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })

        searchView.onActionViewExpanded()
    }

    // NOTE: subscribe logic

    private fun subscribeToPresenter() = (photoSearchPresenter as BasePresenter<PhotoListView>).subscribe(this)

    private fun unsubscribeFromPresenter() = (photoSearchPresenter as BasePresenter<PhotoListView>).unsubscribe()

    // NOTE: life cycle methods

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeFromPresenter()
        photoPresenter.destroyDownloadQueue()
    }
}