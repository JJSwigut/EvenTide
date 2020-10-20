package com.jjswigut.eventide.ui

import com.jjswigut.eventide.data.entities.TidalStation


sealed class StationAction {
    data class StationClicked(val position: Int, val station: TidalStation) : StationAction()
}