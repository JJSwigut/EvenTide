package com.jjswigut.eventide.data.remote

import com.jjswigut.eventide.data.entities.TidalStation
import com.jjswigut.eventide.data.entities.Tide
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {
    @GET("?stations&lat=33.768321&lon=-118.195617&stationDistance=50&key=05b6e10b-e0cc-4a27-b983-29b858395a35")
    suspend fun getStations() : Response<TidalStation>

    @GET("?heights&extremes&date=2020-10-18&lat=33.768321&lon=-118.195617&days=7&key=05b6e10b-e0cc-4a27-b983-29b858395a35")
    suspend fun getTides(): Response<Tide>
}