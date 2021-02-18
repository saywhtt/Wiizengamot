package edu.born.flicility.fragments.abstraction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.born.flicility.adapters.PhotoAdapter
import edu.born.flicility.databinding.FragmentPhotoListBinding
import edu.born.flicility.model.Photo
import edu.born.flicility.views.PhotoListView

abstract class AbstractPhotoListFragment : VisibleFragment<FragmentPhotoListBinding>(), PhotoListView {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPhotoListBinding =
            FragmentPhotoListBinding::inflate

    protected abstract var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        with(binding.fragmentPhotoListRecyclerView) {
            adapter = getPreparedAdapter()
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        return binding.root
    }

    abstract fun getPreparedAdapter(): PhotoAdapter

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
}