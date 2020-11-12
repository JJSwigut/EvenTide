package com.jjswigut.eventide.data.entities


data class Tides(
    val atlas: String,
    val callCount: Int,
    val copyright: String,
    val extremes: List<Extreme>,
    val heights: List<Height>,
    val requestLat: Double,
    val requestLon: Double,
    val responseLat: Double,
    val responseLon: Double,
    val station: String,
    val status: Int
)