package com.jjswigut.eventide.data.repository

import com.jjswigut.eventide.data.local.Dao
import com.jjswigut.eventide.data.remote.RemoteDataSource
import com.jjswigut.eventide.utils.performGetOperation
import javax.inject.Inject

class StationRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: Dao
) {


    fun getStations(lat: Double, lon: Double) = performGetOperation(
        databaseQuery = { localDataSource.getStations() },
        networkCall = { remoteDataSource.getStations(lat, lon) },
        saveCallResult = {
            localDataSource.deleteStations()
            localDataSource.insertStations(it.stations)
        }
    )


}