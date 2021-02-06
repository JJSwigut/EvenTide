package com.jjswigut.eventide.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jjswigut.eventide.data.entities.tidalpredictions.Prediction
import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStation

@Dao
interface Dao {

    @Query("SELECT * FROM prediction_station_table")
    fun getPredictionStations(): LiveData<List<PredictionStation>>

    @Query("SELECT * FROM tide_prediction_table")
    fun getTides(): LiveData<List<Prediction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<PredictionStation>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTides(tides: List<Prediction>)

    @Query("DELETE FROM prediction_station_table")
    suspend fun deleteStations()

    @Query("DELETE FROM tide_prediction_table")
    suspend fun deleteTides()
}
