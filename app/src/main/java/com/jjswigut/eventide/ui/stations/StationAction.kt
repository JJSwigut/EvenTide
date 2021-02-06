package com.jjswigut.eventide.ui.stations

import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStation

sealed class StationAction {
    data class StationClicked(val position: Int, val station: PredictionStation) : StationAction()
}
