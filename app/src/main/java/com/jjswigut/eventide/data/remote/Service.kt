package com.jjswigut.eventide.data.remote


import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStationList
import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionTideList
import retrofit2.Response
import retrofit2.http.GET

import retrofit2.http.Query


interface Service {

    @GET("mdapi/prod/webapi/stations.json?type=tidepredictions&units=english")
    suspend fun getPredictionStations(
    ): Response<PredictionStationList>


    @GET("api/prod/datagetter?product=predictions&application=NOS.COOPS.TAC.WL&datum=MLLW&time_zone=lst_ldt&units=metric&interval=hilo&format=json")
    suspend fun getTides(
        @Query("begin_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("station") stationID: String
    ): Response<PredictionTideList>
}
