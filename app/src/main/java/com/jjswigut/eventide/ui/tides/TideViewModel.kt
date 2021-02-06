package com.jjswigut.eventide.ui.tides

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jjswigut.eventide.data.entities.DayHeader
import com.jjswigut.eventide.data.entities.UIModel
import com.jjswigut.eventide.data.entities.tidalpredictions.Prediction
import com.jjswigut.eventide.data.repository.TideRepository
import com.jjswigut.eventide.utils.Preferences
import com.jjswigut.eventide.utils.Resource

class TideViewModel @ViewModelInject constructor(
    private val repo: TideRepository,
    val prefs: Preferences
) : ViewModel() {

    val tidesLiveData = MutableLiveData<List<Prediction>>()
    var sortedTidesLiveData = MutableLiveData<ArrayList<UIModel>>()

    fun sortTides(list: List<Prediction>?): ArrayList<UIModel> {
        val uiModels = arrayListOf<UIModel>()
        list?.groupBy { it.t.take(10) }?.forEach { map ->
            uiModels.add(UIModel.DayModel(DayHeader(map.key)))
            map.value.forEach { tide ->
                uiModels.add(UIModel.TideModel(tide))
            }
        }
        return uiModels
    }

    fun getTidesWithLocation(station: String): LiveData<Resource<List<Prediction>>> {
        return repo.getTides(station)
    }
}
