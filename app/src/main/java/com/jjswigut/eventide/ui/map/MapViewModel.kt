package com.jjswigut.eventide.ui.map

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStation
import com.jjswigut.eventide.data.repository.StationRepository
import com.jjswigut.eventide.utils.Preferences


class MapViewModel @ViewModelInject constructor(
    private val repo: StationRepository,
    val prefs: Preferences
) : ViewModel() {

    val stationClicked = MutableLiveData<Boolean>()
    var station: PredictionStation? = null
    val stationLiveData
        get() = repo.getPredictionStations()


    fun buildStationList(list: List<PredictionStation>): ArrayList<PredictionStation> {
        val stationList = arrayListOf<PredictionStation>()
        list.forEach { station -> stationList.add(station) }
        return stationList
    }

}





