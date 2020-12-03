package com.jjswigut.eventide.ui.search

import com.jjswigut.eventide.data.entities.TidalStation


sealed class StationAction {
    data class StationClicked(val position: Int, val station: TidalStation) : StationAction()
}