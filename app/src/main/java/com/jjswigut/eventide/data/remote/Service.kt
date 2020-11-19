package com.jjswigut.eventide.data.remote


import com.jjswigut.eventide.data.entities.StationList
import com.jjswigut.eventide.data.entities.TideList
import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query


interface Service {

    @GET("v2?stations&stationDistance=50")
    suspend fun getStations(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") tideApiKey: String
    ): Response<StationList>

    @GET("v2?heights&extremes&days=7")
    suspend fun getTides(
        @Query("date") date: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") tideApiKey: String
    ): Response<Tides>
}