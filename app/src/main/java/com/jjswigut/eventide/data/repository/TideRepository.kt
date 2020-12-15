package com.jjswigut.eventide.data.repository

import com.google.android.gms.maps.model.LatLng
import com.jjswigut.eventide.data.local.Dao
import com.jjswigut.eventide.data.remote.RemoteDataSource
import com.jjswigut.eventide.utils.performGetOperation
import javax.inject.Inject

class TideRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: Dao
) {


    fun getTides(location: LatLng) = performGetOperation(
        databaseQuery = { localDataSource.getTides() },
        networkCall = { remoteDataSource.getTides(location) },
        saveCallResult = {
            localDataSource.deleteTides()
            localDataSource.insertTides(it.extremes)
        }
    )

}