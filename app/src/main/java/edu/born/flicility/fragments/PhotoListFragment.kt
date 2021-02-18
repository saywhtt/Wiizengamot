package edu.born.flicility.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial
import edu.born.flicility.R
import edu.born.flicility.activities.PhotoPagerActivity
import edu.born.flicility.activities.PhotoSearchActivity
import edu.born.flicility.adapters.OnBottomReachedListener
import edu.born.flicility.adapters.OnPhotoClickedListener
import edu.born.flicility.adapters.PhotoAdapter
import edu.born.flicility.fragments.abstraction.AbstractPhotoListFragment
import edu.born.flicility.model.Photo
import edu.born.flicility.presenters.BasePresenter
import edu.born.flicility.presenters.PhotoListPresenter
import edu.born.flicility.presenters.PhotoPresenter
import edu.born.flicility.presenters.Query
import edu.born.flicility.service.isServiceStarted
import edu.born.flicility.service.setServiceStart
import edu.born.flicility.views.PhotoListView
import javax.inject.Inject
import javax.inject.Named

class PhotoListFragment : AbstractPhotoListFragment() {

    companion object {
        private const val PHOTO_DETAIL_REQUEST_CODE = 0
    }

    override lateinit var adapter: PhotoAdapter

    @Inject
    @Named("photoListFragment")
    lateinit var photoListPresenter: PhotoListPresenter

    @Inject
    lateinit var photoPresenter: PhotoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app.plusPhotoComponent().inject(this)
        adapter = PhotoAdapter()
        subscribeToPresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // start query
        photoListPresenter.getPhotos()
    }

    override fun getPreparedAdapter(): PhotoAdapter {
        adapter.onBottomReachedListener = object : OnBottomReachedListener {
            override fun onBottomReached() {
                photoListPresenter.getPhotos()
            }
        }
        adapter.onPhotoClickedListener = object : OnPhotoClickedListener {
            override fun onPhotoClicked(position: Int, photos: ArrayList<Photo>) {
                val intent = PhotoPagerActivity.newIntent(context, position, photos, photoListPresenter.getQuery())
                startActivityForResult(intent, PHOTO_DETAIL_REQUEST_CODE)
            }
        }
        return adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_list, menu)

        context?.let {
            val toggleItem = menu.findItem(R.id.menu_item_toggle_polling).actionView as SwitchMaterial
            toggleItem.isChecked = isServiceStarted(it)
            toggleItem.setOnCheckedChangeListener { _, isOn ->
                photoListPresenter.getPhotos()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        if (requestCode == PHOTO_DETAIL_REQUEST_CODE) {
            data?.let {
                val photos = PhotoPagerActivity.getPhotos(it)
                val position = PhotoPagerActivity.getPosition(it)
                val query = PhotoPagerActivity.getQuery(it) as Query.All
                photoListPresenter.setQuery(query)
                adapter.updateWithStartPosition(photos, position)
            }
        }
    }

    private fun subscribeToPresenter() = (photoListPresenter as BasePresenter<PhotoListView>).subscribe(this)

    private fun unsubscribeFromPresenter() = (photoListPresenter as BasePresenter<PhotoListView>).unsubscribe()

    // NOTE: life cycle methods

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeFromPresenter()
        photoPresenter.destroyDownloadQueue()
    }
}