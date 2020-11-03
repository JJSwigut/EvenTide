package com.jjswigut.eventide.data.remote

import com.jjswigut.eventide.R
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: Service
): BaseDataSource() {

    suspend fun getStations() = getResult { service.getStations(R.string.tides_api_key.toString()) }
    suspend fun getTides() = getResult { service.getTides(R.string.tides_api_key.toString()) }
}