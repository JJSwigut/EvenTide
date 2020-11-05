package com.jjswigut.eventide.data.remote

import com.jjswigut.eventide.BuildConfig
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: Service
): BaseDataSource() {

    private val apiKey = BuildConfig.tideApiKey

    suspend fun getStations() = getResult { service.getStations(apiKey) }
    suspend fun getTides() = getResult { service.getTides(apiKey) }
}