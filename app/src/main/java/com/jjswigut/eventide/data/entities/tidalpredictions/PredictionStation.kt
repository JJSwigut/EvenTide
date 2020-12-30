package com.jjswigut.eventide.data.entities.tidalpredictions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prediction_station_table")
class PredictionStation(

    @PrimaryKey
    val id: String,
    @ColumnInfo
    val state: String,
    @ColumnInfo
    val lat: Double,
    @ColumnInfo
    val lng: Double,
    @ColumnInfo
    val name: String

)