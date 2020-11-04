package com.jjswigut.eventide.data.remote

import com.jjswigut.eventide.data.entities.StationList
import com.jjswigut.eventide.data.entities.TideList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {
    @GET("v2?stations&lat=33.768321&lon=-118.195617&stationDistance=50&key={api_key}")
    suspend fun getStations(
        @Path("api_key") tideApikey: String
    ): Response<StationList>

    @GET("v2?heights&extremes&date=2020-10-18&lat=33.768321&lon=-118.195617&days={api_key}")
    suspend fun getTides(
        @Path("api_key") tideApikey: String
    ): Response<TideList>
}