package com.jjswigut.eventide.data.entities

sealed class UIModel {

    class DayModel(val dayHeader: DayHeader) : UIModel()

    class TideModel(val tideItem: Extreme) : UIModel()
}