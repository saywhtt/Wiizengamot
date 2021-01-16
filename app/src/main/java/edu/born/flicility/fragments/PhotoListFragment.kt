package edu.born.flicility.fragments

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import edu.born.flicility.PhotoDownloader
import edu.born.flicility.R
import edu.born.flicility.adapters.PhotoAdapter
import edu.born.flicility.app.App
import edu.born.flicility.model.Photo
import edu.born.flicility.presenters.BasePresenter
import edu.born.flicility.presenters.PhotoListPresenter
import edu.born.flicility.service.isServiceStarted
import edu.born.flicility.service.setServiceStart
import edu.born.flicility.views.PhotoListView
import javax.inject.Inject

class PhotoListFragment : VisibleFragment(), PhotoListView {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar

    @Inject
    lateinit var photoDownloader: PhotoDownloader

    @Inject
    lateinit var adapter: PhotoAdapter

    @Inject
    lateinit var mPhotoListPresenter: PhotoListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).plusPhotoComponent()
                .inject(this)
        retainInstance = true
        setHasOptionsMenu(true)
        subscribeToPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        mRecyclerView = view.findViewById(R.id.recycler_view)
        mProgressBar = view.findViewById(R.id.progress_view)

        mRecyclerView.layoutManager = GridLayoutManager(activity, 3)

        mPhotoListPresenter.getPhotos()

        if (!photoDownloader.isAlive) photoDownloader.start()

        prepareAdapter()

        return view
    }

    private fun prepareAdapter() {
        mRecyclerView.adapter = adapter
        adapter.onBottomReachedListener = {
            mPhotoListPresenter.getNextPageByCurrentQuery()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)

        val searchView = menu.findItem(R.id.menu_item_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                adapter.deleteAll()
                mPhotoListPresenter.getPhotosByNewQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })

        context?.let {
            val toggleItem = menu.findItem(R.id.menu_item_toggle_polling).actionView as SwitchMaterial
            toggleItem.isChecked = isServiceStarted(it)
            toggleItem.setOnCheckedChangeListener { buttonView, isOn ->
                setServiceStart(it, isOn)
                val toastDescription = if (isOn) R.string.notifications_is_on else R.string.notifications_is_off
                Toast.makeText(it, toastDescription, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear -> {
                adapter.deleteAll()
                mPhotoListPresenter.getPhotos()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeToPresenter() = (mPhotoListPresenter as BasePresenter<PhotoListView>).subscribe(this)

    private fun unsubscribeFromPresenter() = (mPhotoListPresenter as BasePresenter<PhotoListView>).unsubscribe()

    // NOTE: view implementation
    override fun hideKeyboard() {
        activity?.let {
            val imm = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = it.currentFocus ?: View(it)
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun updateData(data: List<Photo>) {
        adapter.insertAll(data)
    }

    override fun startDownloading() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun endDownloading() {
        mProgressBar.visibility = View.GONE
    }

    // NOTE: life cycle methods
    override fun onDestroyView() {
        super.onDestroyView()
        photoDownloader.clearQueue()
    }

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeFromPresenter()
        //photoDownloader.quit()
    }

}