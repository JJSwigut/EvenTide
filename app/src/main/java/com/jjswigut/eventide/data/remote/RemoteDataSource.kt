package com.jjswigut.eventide.data.remote

import com.jjswigut.eventide.BuildConfig
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: Service
): BaseDataSource() {

    private val apiKey = BuildConfig.tideApiKey

    suspend fun getStations(lat: Double, lon: Double) =
        getResult { service.getStations(lat, lon, apiKey) }

}