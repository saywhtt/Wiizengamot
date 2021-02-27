package edu.born.flicility.fragments.abstraction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.born.flicility.activities.SingleFragmentActivity.Companion.PHOTOS_ARG
import edu.born.flicility.activities.SingleFragmentActivity.Companion.PHOTO_POSITION_ARG
import edu.born.flicility.activities.SingleFragmentActivity.Companion.QUERY_ARG
import edu.born.flicility.adapters.OnBottomReachedListener
import edu.born.flicility.adapters.OnPhotoClickedListener
import edu.born.flicility.adapters.PhotoAdapter
import edu.born.flicility.databinding.FragmentPhotoListBinding
import edu.born.flicility.model.Photo
import edu.born.flicility.presenters.BasePhotosPresenter
import edu.born.flicility.utils.START_PHOTO_PAGER_REQUEST_KEY
import edu.born.flicility.views.PhotoListView

abstract class AbstractPhotoListFragment : VisibleFragment<FragmentPhotoListBinding>(), PhotoListView {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoListBinding =
            FragmentPhotoListBinding::inflate

    protected abstract var adapter: PhotoAdapter
    protected abstract var basePhotosPresenter: BasePhotosPresenter<PhotoListView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun setup() = with(binding.fragmentPhotoListRv) {
        adapter = getPreparedAdapter()
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun getPreparedAdapter(): PhotoAdapter {
        adapter.onBottomReachedListener = object : OnBottomReachedListener {
            override fun onBottomReached() = basePhotosPresenter.getPhotos()
        }
        adapter.onPhotoClickedListener = object : OnPhotoClickedListener {
            override fun onPhotoClicked(position: Int, photos: ArrayList<Photo>) {
                setFragmentResult(START_PHOTO_PAGER_REQUEST_KEY, bundleOf(
                        PHOTOS_ARG to photos,
                        PHOTO_POSITION_ARG to position,
                        QUERY_ARG to basePhotosPresenter.query
                ))
                actionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
        return adapter
    }

    // NOTE: view implementation

    override fun update(data: List<Photo>) {
        adapter.update(data)
    }

    override fun startDownloading() {
        binding.fragmentPhotoListPb.visibility = View.VISIBLE
    }

    override fun endDownloading() {
        binding.fragmentPhotoListPb.visibility = View.GONE
    }

    override fun getViewContext() = context

    // NOTE: subscribe logic

    protected fun subscribeToPresenter() = basePhotosPresenter.subscribe(this)

    protected fun unsubscribeFromPresenter() = basePhotosPresenter.unsubscribe()
}