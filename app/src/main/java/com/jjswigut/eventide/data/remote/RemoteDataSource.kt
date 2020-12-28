package com.jjswigut.eventide.data.remote

import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStationList
import com.jjswigut.eventide.utils.Resource
import java.time.LocalDate
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: Service
): BaseDataSource() {

    private val startDate = LocalDate.now().toString().filter { it.isDigit() }
    private val endDate = LocalDate.now().plusWeeks(4).toString().filter { it.isDigit() }


    suspend fun getPredictionStations(): Resource<PredictionStationList> {
        return getResult { service.getPredictionStations() }

    }

    suspend fun getTides(station: String) =
        getResult { service.getTides(startDate, endDate, station) }
}