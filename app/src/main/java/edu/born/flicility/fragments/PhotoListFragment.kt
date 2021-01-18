package edu.born.flicility.fragments

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import edu.born.flicility.PhotoDownloader
import edu.born.flicility.R
import edu.born.flicility.activities.PhotoSearchActivity
import edu.born.flicility.adapters.PhotoAdapter
import edu.born.flicility.model.Photo
import edu.born.flicility.presenters.BasePresenter
import edu.born.flicility.presenters.PhotoListPresenter
import edu.born.flicility.service.isServiceStarted
import edu.born.flicility.service.setServiceStart
import edu.born.flicility.views.PhotoListView
import javax.inject.Inject

class PhotoListFragment : VisibleFragment(), PhotoListView {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: PhotoAdapter

    @Inject
    lateinit var photoDownloader: PhotoDownloader
    @Inject
    lateinit var photoListPresenter: PhotoListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app.plusPhotoComponent().inject(this)
        adapter = PhotoAdapter(photoDownloader)
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

        photoListPresenter.getPhotos()
        if (!photoDownloader.isAlive) photoDownloader.start()

        return view
    }

    private fun getPreparedAdapter(): PhotoAdapter {
        adapter.onBottomReachedListener = { photoListPresenter.getPhotos() }
        return adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_list, menu)

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
            R.id.menu_item_start_search -> {
                val intent = PhotoSearchActivity.newIntent(activity)
                activity?.startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeToPresenter() = (photoListPresenter as BasePresenter<PhotoListView>).subscribe(this)

    private fun unsubscribeFromPresenter() = (photoListPresenter as BasePresenter<PhotoListView>).unsubscribe()

    // NOTE: view implementation

    override fun update(data: List<Photo>) {
        adapter.insertAll(data)
    }

    override fun startDownloading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun endDownloading() {
        progressBar.visibility = View.GONE
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