package com.jjswigut.eventide.data.remote

import com.google.android.gms.maps.model.LatLng
import com.jjswigut.eventide.BuildConfig
import java.time.LocalDate
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: Service
): BaseDataSource() {

    private val apiKey = BuildConfig.tideApiKey
    private val date = LocalDate.now().toString()

    suspend fun getStations(location: LatLng) =
        getResult { service.getStations(location.latitude, location.longitude, apiKey) }

    suspend fun getTides(location: LatLng) =
        getResult { service.getTides(date, location.latitude, location.longitude, apiKey) }
}