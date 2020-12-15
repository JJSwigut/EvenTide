package com.jjswigut.eventide.ui.map

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.entities.TidalStation
import com.jjswigut.eventide.data.repository.StationRepository
import com.jjswigut.eventide.utils.Preferences


class MapViewModel @ViewModelInject constructor(
    private val repo: StationRepository,
    val prefs: Preferences
) : ViewModel() {


    val stationLiveData
        get() = repo.getStations(
            prefs.userLocation
        )


    fun buildStationList(list: List<TidalStation>): ArrayList<TidalStation> {
        val stationList = arrayListOf<TidalStation>()
        list.forEach { station -> stationList.add(station) }
        return stationList
    }

}





