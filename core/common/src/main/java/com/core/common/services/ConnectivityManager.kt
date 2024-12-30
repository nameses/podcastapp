package com.core.common.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat.getSystemService
import javax.inject.Inject
import javax.inject.Singleton

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = getSystemService(context, ConnectivityManager::class.java)

    val network = connectivityManager?.activeNetwork
    val capabilities = connectivityManager?.getNetworkCapabilities(network)
    return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}