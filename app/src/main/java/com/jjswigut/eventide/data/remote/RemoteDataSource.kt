package com.jjswigut.eventide.data.remote

import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStationList
import com.jjswigut.eventide.utils.Resource
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: Service
) : BaseDataSource() {

    private val fourWeeksInMilliseconds = 2419200000
    private val formatter = SimpleDateFormat("yyyyMMdd", Locale.US)
    private val systemTime = Calendar.getInstance().time
    private val endSystemTime = systemTime.time + fourWeeksInMilliseconds
    private val startDate = formatter.format(systemTime)
    private val endDate = formatter.format(endSystemTime)

    suspend fun getPredictionStations(): Resource<PredictionStationList> {
        return getResult { service.getPredictionStations() }
    }

    suspend fun getTides(station: String) =
        getResult { service.getTides(startDate, endDate, station) }
}