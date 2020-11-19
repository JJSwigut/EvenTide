package com.jjswigut.eventide.data.repository

import com.jjswigut.eventide.data.local.Dao
import com.jjswigut.eventide.data.remote.RemoteDataSource
import com.jjswigut.eventide.utils.performGetOperation
import javax.inject.Inject

class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: Dao

) {


    fun getTides() = performGetOperation(
        databaseQuery = { localDataSource.getTides() },
        networkCall = { remoteDataSource.getTides() },
        saveCallResult = { localDataSource.insertTides(it.extremes) }
    )

    fun getStations(lat: Double, lon: Double) = performGetOperation(
        databaseQuery = { localDataSource.getStations() },
        networkCall = { remoteDataSource.getStations(lat, lon) },
        saveCallResult = { localDataSource.insertStations(it.stations) }
    )



}