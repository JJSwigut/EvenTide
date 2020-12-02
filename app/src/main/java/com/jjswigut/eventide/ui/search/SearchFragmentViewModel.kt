package com.jjswigut.eventide.ui.search

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.entities.Extreme
import com.jjswigut.eventide.data.entities.TidalStation
import com.jjswigut.eventide.data.repository.Repository
import com.jjswigut.eventide.utils.Resource


class SearchFragmentViewModel @ViewModelInject constructor(
    private val repo: Repository
) : ViewModel() {

    private var mockLocation = Location("")

    val userLocation = MutableLiveData<Location>()
    val stationLiveData = MutableLiveData<List<TidalStation>>()
    val tidesLiveData = MutableLiveData<List<Extreme>>()

    init {
        mockLocation.latitude = 41.3543
        mockLocation.longitude = -71.9665
        userLocation.value = mockLocation

    }


    fun getStationsWithLocation(location: Location): LiveData<Resource<List<TidalStation>>> {
        return repo.getStations(location.latitude, location.longitude)

    }

    fun getTidesWithLocation(location: Location): LiveData<Resource<List<Extreme>>> {
        return repo.getTides(location.latitude, location.longitude)
    }

}




