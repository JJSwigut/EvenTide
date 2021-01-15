package com.jjswigut.eventide.ui.map

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.entities.TideCard
import com.jjswigut.eventide.data.entities.tidalpredictions.Prediction
import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStation
import com.jjswigut.eventide.data.repository.StationRepository
import com.jjswigut.eventide.data.repository.TideRepository
import com.jjswigut.eventide.utils.Preferences
import com.jjswigut.eventide.utils.Resource


class MapViewModel @ViewModelInject constructor(
    private val stationRepo: StationRepository,
    private val tideRepo: TideRepository,
    val prefs: Preferences

) : ViewModel() {

    val stationClicked = MutableLiveData<Boolean>()
    var station: PredictionStation? = null
    val stationLiveData
        get() = stationRepo.getPredictionStations()

    val tidesLiveData = MutableLiveData<List<Prediction>>()


    fun buildStationList(list: List<PredictionStation>): ArrayList<PredictionStation> {
        val stationList = arrayListOf<PredictionStation>()
        list.forEach { station -> stationList.add(station) }
        return stationList
    }

    fun getTidesWithLocation(station: String): LiveData<Resource<List<Prediction>>> {
        return tideRepo.getTides(station)

    }

    fun sortTidesForMapCards(list: List<Prediction>?): ArrayList<TideCard> {
        val cardModels = arrayListOf<TideCard>()
        list?.groupBy { it.t.take(10) }?.forEach { map ->
            cardModels.add(TideCard(map.key, map.value))
        }
        return cardModels
    }

}





