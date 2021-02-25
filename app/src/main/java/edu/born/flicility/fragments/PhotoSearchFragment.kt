package edu.born.flicility.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.SearchView
import androidx.fragment.app.setFragmentResultListener
import edu.born.flicility.R
import edu.born.flicility.activities.SingleFragmentActivity.Companion.END_PHOTO_PAGER_BY_SEARCH_REQUEST_KEY
import edu.born.flicility.adapters.PhotoAdapter
import edu.born.flicility.fragments.abstraction.AbstractPhotoListFragment
import edu.born.flicility.presenters.BasePhotosPresenter
import edu.born.flicility.presenters.PhotoPresenter
import edu.born.flicility.presenters.PhotoSearchPresenter
import edu.born.flicility.views.PhotoListView
import javax.inject.Inject


class PhotoSearchFragment : AbstractPhotoListFragment() {

    override lateinit var adapter: PhotoAdapter

    @Inject
    lateinit var photoSearchPresenter: PhotoSearchPresenter<PhotoListView>

    @Inject
    lateinit var photoPresenter: PhotoPresenter

    override lateinit var basePhotosPresenter: BasePhotosPresenter<PhotoListView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app.plusPhotoComponent().inject(this)
        adapter = PhotoAdapter()
        basePhotosPresenter = photoSearchPresenter
        subscribeToPresenter()
    }

    override fun setup() {
        super.setup()
        setFragmentResultListener(END_PHOTO_PAGER_BY_SEARCH_REQUEST_KEY) { _, result ->
            val (photos, position, query) = PhotoPagerFragment.splitArguments(result)
            photoSearchPresenter.query = query
            adapter.updateWithStartPosition(photos, position)
        }
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
        if (!blockReCreatedFromBackStack) { // set focus
            searchView.onActionViewExpanded()
            blockReCreatedFromBackStack = true
        } else { // set focus w/ query text and w/o up keyboard
            searchView.setQuery(photoSearchPresenter.query.text, false)
            searchView.isIconified = false
            searchView.clearFocus()
        }

    }

    // NOTE: life cycle methods

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeFromPresenter()
        photoPresenter.destroyDownloadQueue()
    }
}