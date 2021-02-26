package edu.born.flicility.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun Context.isNetworkAvailableAndConnected(): Boolean {
    val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
}