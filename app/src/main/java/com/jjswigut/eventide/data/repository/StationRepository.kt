package com.jjswigut.eventide.data.repository

import com.jjswigut.eventide.data.local.Dao
import com.jjswigut.eventide.data.remote.RemoteDataSource
import com.jjswigut.eventide.utils.performGetOperation
import javax.inject.Inject

class StationRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: Dao
) {


    fun getPredictionStations() = performGetOperation(
        databaseQuery = { localDataSource.getPredictionStations() },
        networkCall = { remoteDataSource.getPredictionStations() },
        saveCallResult = {
            localDataSource.deleteStations()
            localDataSource.insertStations(it.stations)
        }
    )


}