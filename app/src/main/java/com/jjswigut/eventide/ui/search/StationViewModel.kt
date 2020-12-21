package com.jjswigut.eventide.ui.search

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.entities.TidalStation
import com.jjswigut.eventide.data.repository.StationRepository
import com.jjswigut.eventide.utils.Preferences
import com.jjswigut.eventide.utils.Resource


class StationViewModel @ViewModelInject constructor(
    private val repo: StationRepository,
    prefs: Preferences
) : ViewModel() {

    val preferences = prefs
    val userLocation = MutableLiveData<Location>()
    val stationLiveData = MutableLiveData<List<TidalStation>>()

    fun getStationsWithLocation(location: Location): LiveData<Resource<List<TidalStation>>> {
        return repo.getStations(location.latitude, location.longitude)

    }

}





