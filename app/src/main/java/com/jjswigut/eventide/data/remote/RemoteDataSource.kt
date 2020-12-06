package com.jjswigut.eventide.data.remote

import com.jjswigut.eventide.BuildConfig
import java.time.LocalDate
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: Service
): BaseDataSource() {

    private val apiKey = BuildConfig.tideApiKey
    private val date = LocalDate.now().toString()

    suspend fun getStations(lat: Double, lon: Double) =
        getResult { service.getStations(lat, lon, apiKey) }

    suspend fun getTides(lat: Double, lon: Double) =
        getResult { service.getTides(date, lat, lon, apiKey) }
}