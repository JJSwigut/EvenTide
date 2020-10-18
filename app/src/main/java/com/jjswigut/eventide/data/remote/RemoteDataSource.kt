package com.jjswigut.eventide.data.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: Service
): BaseDataSource() {

    suspend fun getStations() = getResult { service.getStations() }
    suspend fun getTides() = getResult { service.getTides() }
}