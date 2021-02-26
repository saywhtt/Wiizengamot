package edu.born.flicility.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.setFragmentResult
import edu.born.flicility.activities.SingleFragmentActivity.Companion.END_PHOTO_PAGER_BY_ALL_REQUEST_KEY
import edu.born.flicility.activities.SingleFragmentActivity.Companion.END_PHOTO_PAGER_BY_SEARCH_REQUEST_KEY
import edu.born.flicility.activities.SingleFragmentActivity.Companion.CLOSE_FRAGMENT_REQUEST_KEY
import edu.born.flicility.databinding.FragmentPhotoPagerBinding
import edu.born.flicility.fragments.abstraction.VisibleFragment
import edu.born.flicility.model.Photo
import edu.born.flicility.presenters.*
import edu.born.flicility.views.PhotoListView
import javax.inject.Inject
import javax.inject.Named

class PhotoPagerFragment : VisibleFragment<FragmentPhotoPagerBinding>(), PhotoListView {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoPagerBinding =
            FragmentPhotoPagerBinding::inflate

    companion object {
        private const val PHOTOS_ARG = "PHOTOS_ARG"
        private const val PHOTO_POSITION_ARG = "PHOTO_POSITION_ARG"
        private const val QUERY_ARG = "QUERY_ARG"

        fun newInstance(position: Int, photos: MutableList<Photo>, query: Query) =
                PhotoPagerFragment().apply {
                    arguments = bundleOf(
                            PHOTOS_ARG to photos,
                            PHOTO_POSITION_ARG to position,
                            QUERY_ARG to query
                    )
                }

        data class PhotoPagerArguments(val photos: MutableList<Photo>, val position: Int, val query: Query)

        fun splitArguments(result: Bundle) = with(result) {
            val photos = requireNotNull(result.getParcelableArrayList<Photo>(PHOTOS_ARG))
            val position = result.getInt(PHOTO_POSITION_ARG)
            val query = requireNotNull(result.getParcelable<Query>(QUERY_ARG))
            PhotoPagerArguments(photos, position, query)
        }
    }

    @Inject
    @Named("photoPagerActivity")
    lateinit var photoListPresenter: PhotoListPresenter<PhotoListView>

    @Inject
    lateinit var photoSearchPresenter: PhotoSearchPresenter<PhotoListView>

    lateinit var basePhotosPresenter: BasePhotosPresenter<PhotoListView>

    private var photos = mutableListOf<Photo>()
    private var photosDownloadedByPager = mutableListOf<Photo>()
    private var currentItemPosition = 0
    private lateinit var query: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app.plusPhotoComponent().inject(this)
        photos = requireNotNull(arguments?.getParcelableArrayList<Photo>(PHOTOS_ARG))
        query = requireNotNull(arguments?.getParcelable(QUERY_ARG))
    }

    override fun setup() {
        executeByQuery({
            basePhotosPresenter = photoListPresenter
        }, {
            basePhotosPresenter = photoSearchPresenter
        })
        subscribeToPresenter()
        with(binding) {
            fragmentPhotoViewPager.adapter = object : FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                override fun getItem(position: Int): PhotoFragment {
                    if (photos.size - 1 == position) {
                        basePhotosPresenter.getPhotos()
                    }
                    return PhotoFragment.newInstance(photos[position])
                }

                override fun getCount() = photos.size

                override fun setPrimaryItem(container: ViewGroup, position: Int, any: Any) {
                    super.setPrimaryItem(container, position, any)
                    currentItemPosition = position
                }
            }
            fragmentPhotoViewPager.currentItem = requireNotNull(
                    arguments?.getInt(PHOTO_POSITION_ARG, currentItemPosition)
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            setFragmentResult(CLOSE_FRAGMENT_REQUEST_KEY, bundleOf())
            setFragmentResult(getRequestKey(), bundleOf(
                    PHOTOS_ARG to photosDownloadedByPager,
                    PHOTO_POSITION_ARG to currentItemPosition,
                    QUERY_ARG to getQueryKey()))
        }
    }

    private fun subscribeToPresenter() = basePhotosPresenter.subscribe(this)

    private fun unsubscribeFromPresenter() = basePhotosPresenter.unsubscribe()

    private fun executeByQuery(byAll: () -> Unit, bySearch: () -> Unit) =
            when (query.type) {
                QueryType.ALL -> byAll.invoke()
                QueryType.SEARCH -> bySearch.invoke()
            }

    private fun getRequestKey() =
            when (query.type) {
                QueryType.ALL -> END_PHOTO_PAGER_BY_ALL_REQUEST_KEY
                QueryType.SEARCH -> END_PHOTO_PAGER_BY_SEARCH_REQUEST_KEY
            }

    private fun getQueryKey() =
            when (query.type) {
                QueryType.ALL -> photoListPresenter.query
                QueryType.SEARCH -> photoSearchPresenter.query
            }

    // NOTE: view implementation

    override fun update(data: List<Photo>) {
        photos.addAll(data)
        photosDownloadedByPager.addAll(data)
        binding.fragmentPhotoViewPager.adapter?.notifyDataSetChanged()
    }

    override fun startDownloading() {}

    override fun endDownloading() {}

    override fun getViewContext() = context

    // NOTE: life cycle methods

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeFromPresenter()
    }
}