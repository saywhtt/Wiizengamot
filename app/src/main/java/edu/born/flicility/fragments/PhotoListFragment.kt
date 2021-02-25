package edu.born.flicility.fragments

/*import edu.born.flicility.service.isServiceStarted
import edu.born.flicility.service.setServiceStart*/
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import edu.born.flicility.R
import edu.born.flicility.activities.SingleFragmentActivity.Companion.END_PHOTO_PAGER_BY_ALL_REQUEST_KEY
import edu.born.flicility.activities.SingleFragmentActivity.Companion.START_SEARCH_REQUEST_KEY
import edu.born.flicility.adapters.PhotoAdapter
import edu.born.flicility.fragments.abstraction.AbstractPhotoListFragment
import edu.born.flicility.presenters.BasePhotosPresenter
import edu.born.flicility.presenters.PhotoListPresenter
import edu.born.flicility.presenters.PhotoPresenter
import edu.born.flicility.views.PhotoListView
import javax.inject.Inject
import javax.inject.Named

class PhotoListFragment : AbstractPhotoListFragment() {

    override lateinit var adapter: PhotoAdapter

    @Inject
    @Named("photoListFragment")
    lateinit var photoListPresenter: PhotoListPresenter<PhotoListView>

    @Inject
    lateinit var photoPresenter: PhotoPresenter

    override lateinit var basePhotosPresenter: BasePhotosPresenter<PhotoListView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app.plusPhotoComponent().inject(this)
        basePhotosPresenter = photoListPresenter
        adapter = PhotoAdapter()
        subscribeToPresenter()
    }

    override fun setup() {
        super.setup()
        setFragmentResultListener(END_PHOTO_PAGER_BY_ALL_REQUEST_KEY) { _, result ->
            val (photos, position, query) = PhotoPagerFragment.splitArguments(result)
            photoListPresenter.query = query
            adapter.updateWithStartPosition(photos, position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!blockReCreatedFromBackStack) {
            photoListPresenter.getPhotos()
            blockReCreatedFromBackStack = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_list, menu)

        context?.let {
            /*val toggleItem = menu.findItem(R.id.menu_item_toggle_polling).actionView as SwitchMaterial
            toggleItem.isChecked = isServiceStarted(it)
            toggleItem.setOnCheckedChangeListener { _, isOn ->
                photoListPresenter.getPhotos()
                setServiceStart(it, isOn)
                val toastDescription = if (isOn) R.string.notifications_is_on else R.string.notifications_is_off
                Toast.makeText(it, toastDescription, Toast.LENGTH_LONG).show()
            }*/
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_start_search -> {
                setFragmentResult(START_SEARCH_REQUEST_KEY, bundleOf())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // NOTE: life cycle methods

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeFromPresenter()
    }
}