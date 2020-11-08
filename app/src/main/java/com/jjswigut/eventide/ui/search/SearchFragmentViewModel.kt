package com.jjswigut.eventide.ui.search

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.entities.TidalStation
import com.jjswigut.eventide.data.repository.Repository


class SearchFragmentViewModel @ViewModelInject constructor(
        private val repo: Repository
) : ViewModel() {

    val userLocation = MutableLiveData<Location>()
    val stationLiveData = MutableLiveData<List<TidalStation>>()

    fun getStationsWithLocation(location: Location) {
        val tideStations = repo.getStations(location.latitude, location.longitude)
        stationLiveData.value = tideStations.value?.data
    }

}

