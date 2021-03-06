package com.jjswigut.eventide.ui.stations

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStation
import com.jjswigut.eventide.data.repository.StationRepository
import com.jjswigut.eventide.utils.Preferences
import com.jjswigut.eventide.utils.Resource

class StationViewModel @ViewModelInject constructor(
    private val repo: StationRepository,
    prefs: Preferences
) : ViewModel() {

    val preferences = prefs
    val userLocation = MutableLiveData<LatLng>()
    val stationLiveData = MutableLiveData<List<PredictionStation>>()

    fun getPredictionStations(): LiveData<Resource<List<PredictionStation>>> {
        return repo.getPredictionStations()
    }

    fun sortStationsByDistance(user: LatLng): List<PredictionStation>? {
        return stationLiveData.value?.sortedBy { distance(it, user) }
    }

    fun saveNearestStation(stations: List<PredictionStation>) {
        preferences.saveNearestStationId(stations.first().id)
        preferences.saveNearestStationName(stations.first().name)
    }

    private fun distance(station: PredictionStation, user: LatLng): Float {
        val stationLocation = Location("")
        stationLocation.latitude = station.lat
        stationLocation.longitude = station.lng
        val userLocation = Location("")
        userLocation.latitude = user.latitude
        userLocation.longitude = user.longitude
        return userLocation
            .distanceTo(stationLocation)
    }
}
