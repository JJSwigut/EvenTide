package com.jjswigut.eventide.data.entities

import com.jjswigut.eventide.data.entities.tidalpredictions.Prediction

data class TideCard(val date: String, val list: List<Prediction>)
