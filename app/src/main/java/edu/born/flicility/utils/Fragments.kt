package edu.born.flicility.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*

const val START_SEARCH_REQUEST_KEY = "START_SEARCH_REQUEST_KEY"
const val START_PHOTO_PAGER_REQUEST_KEY = "START_PHOTO_PAGER_REQUEST_KEY"
const val CLOSE_FRAGMENT_REQUEST_KEY = "END_PHOTO_PAGER_REQUEST_KEY"
const val END_PHOTO_PAGER_BY_ALL_REQUEST_KEY = "END_PHOTO_PAGER_BY_ALL_REQUEST_KEY"
const val END_PHOTO_PAGER_BY_SEARCH_REQUEST_KEY = "END_PHOTO_PAGER_BY_SEARCH_REQUEST_KEY"

fun AppCompatActivity.setFragmentResultListener(requestKey: String,
                                                listener: FragmentResultListener) {
    supportFragmentManager.setFragmentResultListener(
            requestKey,
            this,
            listener
    )
}

inline fun <reified T : Fragment> AppCompatActivity.replaceFragment(resId: Int, addBackStack: Boolean = true) {
    supportFragmentManager.commit {
        replace<T>(resId)
        setReorderingAllowed(true)
        if (addBackStack)
            addToBackStack(T::class.java.name)
    }
}

inline fun <reified T : Fragment> AppCompatActivity.addFragment(resId: Int, addBackStack: Boolean = true) {
    supportFragmentManager.commit {
        add<T>(resId)
        setReorderingAllowed(true)
        if (addBackStack)
            addToBackStack(T::class.java.name)
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, resId: Int, addBackStack: Boolean = true) {
    supportFragmentManager.commit {
        replace(resId, fragment)
        setReorderingAllowed(true)
        if (addBackStack)
            addToBackStack(fragment::class.java.name)
    }
}