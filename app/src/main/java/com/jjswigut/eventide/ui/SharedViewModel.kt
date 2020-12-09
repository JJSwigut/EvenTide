package com.jjswigut.eventide.ui

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.entities.DayHeader
import com.jjswigut.eventide.data.entities.Extreme
import com.jjswigut.eventide.data.entities.TidalStation
import com.jjswigut.eventide.data.entities.UIModel
import com.jjswigut.eventide.data.repository.Repository
import com.jjswigut.eventide.utils.Preferences
import com.jjswigut.eventide.utils.Resource


class SharedViewModel @ViewModelInject constructor(
    private val repo: Repository,
    prefs: Preferences
) : ViewModel() {

//    private var mockLocation = Location("")

    val userLocation = MutableLiveData<Location>()
    val stationLiveData = MutableLiveData<List<TidalStation>>()
    val sortedTidesLiveData = MutableLiveData<ArrayList<UIModel>>()
    val preferences = prefs

//    init {
//        mockLocation.latitude = 41.3543
//        mockLocation.longitude = -71.9665
//        //userLocation.value = mockLocation
//
//    }


    fun getStationsWithLocation(location: Location): LiveData<Resource<List<TidalStation>>> {
        return repo.getStations(location.latitude, location.longitude)

    }

    fun getTidesWithLocation(location: Location): LiveData<Resource<List<Extreme>>> {
        return repo.getTides(location.latitude, location.longitude)
    }

    fun sortTides(list: List<Extreme>): ArrayList<UIModel> {
        val sordidTides = list.groupBy { it.date.take(10) }
        val uiModels = arrayListOf<UIModel>()
        sordidTides.forEach { map ->
            uiModels.add(UIModel.DayModel(DayHeader(map.key)))
            map.value.forEach { extreme ->
                uiModels.add(UIModel.TideModel(extreme))
            }
        }
        return uiModels
    }
}





