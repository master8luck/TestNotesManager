package com.masterluck.testnotesmanager.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    val PREF_NAME = "com.masterluck.testnotesmanager.preferences"
    val FIRST_LAUNCH_KEY = "is first launched"

    // Check weather string format of date of note is the same that today's
    // using @formatForCheck format

    private val formatForCheck = SimpleDateFormat("yyyyMMdd")
    private val formatToday = SimpleDateFormat("hh:mm")
    private val formatPast = SimpleDateFormat("dd.MM.yyyy")

    fun formatDate(time: Long): String {
        val date = Date(time)
        return if (formatForCheck.format(Calendar.getInstance().time) == formatForCheck.format(date))
            formatToday.format(date)
        else
            formatPast.format(date)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }


    fun isFirstLaunched(context: Context) : Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if (!pref.contains(FIRST_LAUNCH_KEY)) {
            with(pref.edit()) {
                putBoolean(FIRST_LAUNCH_KEY, true)
                apply()
            }
        }
        return pref.getBoolean(FIRST_LAUNCH_KEY, true)
    }

    fun markLaunched(context: Context) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(pref.edit()) {
            putBoolean(FIRST_LAUNCH_KEY, false)
            apply()
        }
    }

}
