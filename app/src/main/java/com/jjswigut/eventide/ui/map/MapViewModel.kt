package com.jjswigut.eventide.ui.map

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStation
import com.jjswigut.eventide.data.repository.StationRepository
import com.jjswigut.eventide.utils.Preferences


class MapViewModel @ViewModelInject constructor(
    private val repo: StationRepository,
    val prefs: Preferences
) : ViewModel() {

    var stationClicked: Boolean = false
    var stationLatLng: LatLng? = null
    val stationLiveData
        get() = repo.getPredictionStations()


    fun buildStationList(list: List<PredictionStation>): ArrayList<PredictionStation> {
        val stationList = arrayListOf<PredictionStation>()
        list.forEach { station -> stationList.add(station) }
        return stationList
    }

}





