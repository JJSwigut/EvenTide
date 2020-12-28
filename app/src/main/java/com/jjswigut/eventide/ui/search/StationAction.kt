package com.jjswigut.eventide.ui.search


import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStation


sealed class StationAction {
    data class StationClicked(val position: Int, val station: PredictionStation) : StationAction()
}