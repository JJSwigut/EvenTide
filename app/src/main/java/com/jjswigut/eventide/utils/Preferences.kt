package com.jjswigut.eventide.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(@ApplicationContext context: Context) {
    val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val units: Boolean
        get() = prefs.getBoolean("units", false)
    val darkMode: Boolean
        get() = prefs.getBoolean("darkmode", false)

    fun saveLocation(location: Location) {
        prefs.edit {
            putFloat("lat", location.latitude.toFloat())
            putFloat("lon", location.longitude.toFloat())
            Log.d(TAG, "saveLocation: $location")
        }
    }

    fun saveNearestStationId(station: String) {
        prefs.edit {
            putString("stationId", station)
            Log.d(TAG, "saveNearestStation: $station")
        }
    }

    fun saveNearestStationName(station: String) {
        prefs.edit {
            putString("stationName", station)
        }
    }

    val nearestStationName: String?
        get() = prefs.getString("stationName", "")

    val nearestStationId: String?
        get() = prefs.getString("stationId", "")

    val userLocation: LatLng
        get() = LatLng(
            prefs.getFloat("lat", 41.3279F).toDouble(),
            prefs.getFloat("lon", -71.9906F).toDouble()
        )
}
