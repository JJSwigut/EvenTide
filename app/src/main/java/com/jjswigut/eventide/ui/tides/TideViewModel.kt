package com.jjswigut.eventide.ui.tides

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.entities.DayHeader
import com.jjswigut.eventide.data.entities.Extreme
import com.jjswigut.eventide.data.entities.UIModel
import com.jjswigut.eventide.data.repository.TideRepository
import com.jjswigut.eventide.utils.Preferences


class TideViewModel @ViewModelInject constructor(
    private val repo: TideRepository,
    val prefs: Preferences
) : ViewModel() {


    private val userLocation = prefs.userLocation
    val tidesLiveData
        get() = repo.getTides(
            userLocation
        )
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
}





