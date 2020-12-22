package com.jjswigut.eventide.ui.tides

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.jjswigut.eventide.data.entities.DayHeader
import com.jjswigut.eventide.data.entities.Extreme
import com.jjswigut.eventide.data.entities.UIModel
import com.jjswigut.eventide.data.repository.TideRepository
import com.jjswigut.eventide.utils.Preferences
import com.jjswigut.eventide.utils.Resource


class TideViewModel @ViewModelInject constructor(
    private val repo: TideRepository,
    val prefs: Preferences
) : ViewModel() {


    val tidesLiveData = MutableLiveData<List<Extreme>>()
    var sortedTidesLiveData = MutableLiveData<ArrayList<UIModel>>()

    fun sortTides(list: List<Extreme>?): ArrayList<UIModel> {
        val uiModels = arrayListOf<UIModel>()
        list?.groupBy { it.date.take(10) }?.forEach { map ->
            uiModels.add(UIModel.DayModel(DayHeader(map.key)))
            map.value.forEach { extreme ->
                uiModels.add(UIModel.TideModel(extreme))
            }
        }
        return uiModels
    }

    fun getTidesWithLocation(location: LatLng): LiveData<Resource<List<Extreme>>> {
        return repo.getTides(location)

    }
}





