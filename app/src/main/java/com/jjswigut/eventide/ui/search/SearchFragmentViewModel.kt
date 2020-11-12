package com.jjswigut.eventide.ui.search

import android.location.Location
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.entities.Extreme
import com.jjswigut.eventide.data.entities.TidalStation
import com.jjswigut.eventide.data.repository.Repository
import com.jjswigut.eventide.ui.tides.TidesListAdapter
import com.jjswigut.eventide.utils.Resource


class SearchFragmentViewModel @ViewModelInject constructor(
    private val repo: Repository
) : ViewModel() {

    val userLocation = MutableLiveData<Location>()
    val stationLiveData = MutableLiveData<List<TidalStation>>()
    val tidesLiveData = MutableLiveData<List<Extreme>>()
    var sordidTides: List<TidesListAdapter.DataItem> = sortList(tidesLiveData.value)


    fun getStationsWithLocation(location: Location): LiveData<Resource<List<TidalStation>>> {
        return repo.getStations(location.latitude, location.longitude)

    }

    fun getTidesWithLocation(location: Location): LiveData<Resource<List<Extreme>>> {
        return repo.getTides(location.latitude, location.longitude)
    }

    private fun sortList(list: List<Extreme>?): List<TidesListAdapter.DataItem> {
        val sordidList = list?.groupBy { it.date.take(10) }
        val myList = ArrayList<TidesListAdapter.DataItem>()

        if (sordidList != null) {
            for (i in sordidList.keys) {
                myList.add(TidesListAdapter.DataItem.Day(i))
                for (v in sordidList.getValue(i)) {
                    myList.add(TidesListAdapter.DataItem.TideItem(v))
                }
            }
        }
        return myList
    }
}




