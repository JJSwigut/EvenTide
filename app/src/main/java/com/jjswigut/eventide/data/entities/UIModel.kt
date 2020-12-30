package com.jjswigut.eventide.data.entities

import com.jjswigut.eventide.data.entities.tidalpredictions.Prediction

sealed class UIModel {

    class DayModel(val dayHeader: DayHeader) : UIModel()

    class TideModel(val tideItem: Prediction) : UIModel()
}