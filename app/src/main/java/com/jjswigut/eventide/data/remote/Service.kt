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

    @GET("datagetter?date=today&station=9414525&product=high_low&units=english&time_zone=lst&application=Tides_and_Currents&format=json")
    suspend fun getTides(
        @Query("station") stationId: String
    ): Response<TideList>
}