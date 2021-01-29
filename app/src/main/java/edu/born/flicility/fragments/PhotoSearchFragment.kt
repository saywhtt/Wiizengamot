package edu.born.flicility.fragments

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.born.flicility.PhotoDownloader
import edu.born.flicility.R
import edu.born.flicility.activities.PhotoPagerActivity
import edu.born.flicility.adapters.OnBottomReachedListener
import edu.born.flicility.adapters.OnPhotoClickedListener
import edu.born.flicility.adapters.PhotoAdapter
import edu.born.flicility.model.Photo
import edu.born.flicility.presenters.BasePresenter
import edu.born.flicility.presenters.PhotoPresenter
import edu.born.flicility.presenters.PhotoSearchPresenter
import edu.born.flicility.views.PhotoSearchView
import javax.inject.Inject

class PhotoSearchFragment : VisibleFragment(), PhotoSearchView {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: PhotoAdapter

    @Inject
    lateinit var photoSearchPresenter: PhotoSearchPresenter

    @Inject
    lateinit var photoPresenter: PhotoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app.plusPhotoComponent().inject(this)
        adapter = PhotoAdapter(photoPresenter)

        retainInstance = true
        setHasOptionsMenu(true)
        subscribeToPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photo_list, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = getPreparedAdapter()
        recyclerView.layoutManager = GridLayoutManager(activity, 3)
        progressBar = view.findViewById(R.id.progress_view)

        return view
    }

    private fun getPreparedAdapter(): PhotoAdapter {
        adapter.onBottomReachedListener = object : OnBottomReachedListener {
            override fun onBottomReached() {
                photoSearchPresenter.getNextPhotosByCurrentQuery()
            }
        }
        adapter.onPhotoClickedListener = object : OnPhotoClickedListener {
            override fun onPhotoClicked(position: Int, photos: ArrayList<Photo>) {
                val intent = PhotoPagerActivity.newIntent(context, position, photos)
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

    private fun subscribeToPresenter() = (photoSearchPresenter as BasePresenter<PhotoSearchView>).subscribe(this)

    private fun unsubscribeFromPresenter() = (photoSearchPresenter as BasePresenter<PhotoSearchView>).unsubscribe()

    // NOTE: view implementation

    override fun startDownloading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun endDownloading() {
        progressBar.visibility = View.GONE
    }

    override fun update(data: List<Photo>) {
        adapter.insertAll(data)
    }

    override fun hideKeyboard() {
        super.hideKeyboard()
    }

    override fun getViewContext() = context

    // NOTE: life cycle methods

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeFromPresenter()
        photoPresenter.destroyDownloadQueue()
    }
}