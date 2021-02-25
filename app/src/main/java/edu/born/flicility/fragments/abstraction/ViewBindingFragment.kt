package edu.born.flicility.fragments.abstraction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class ViewBindingFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        setup()
        return _binding?.root
    }

    protected var blockAsyncCalls = false

    protected abstract fun setup()

    override fun onDestroyView() {
        super.onDestroyView()
        blockAsyncCalls = true
        _binding = null
    }
}