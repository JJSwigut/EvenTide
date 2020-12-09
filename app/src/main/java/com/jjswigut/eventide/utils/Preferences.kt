package com.jjswigut.eventide.utils

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val units: Boolean
        get() = prefs.getBoolean("units", false)
    val darkMode: Boolean
        get() = prefs.getBoolean("darkmode", false)

    fun saveLocation(location: Location) {
        prefs.edit {
            putFloat("lat", location.latitude.toFloat())
            putFloat("lon", location.longitude.toFloat())
        }
    }

    val userLocation: LatLng
        get() = LatLng(
            prefs.getFloat("lat", 0.0F).toDouble(),
            prefs.getFloat("lon", 0.0F).toDouble()
        )
}
