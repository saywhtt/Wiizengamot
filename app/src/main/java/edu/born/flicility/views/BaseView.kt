package edu.born.flicility.views

import android.content.Context

interface BaseView {
    fun getViewContext(): Context?
    fun startDownloading()
    fun endDownloading()
}