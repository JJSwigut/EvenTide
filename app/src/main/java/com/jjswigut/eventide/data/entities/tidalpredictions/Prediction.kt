package com.jjswigut.eventide.data.entities.tidalpredictions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tide_prediction_table")
data class Prediction(
    @PrimaryKey
    val t: String,
    @ColumnInfo
    val type: String,
    @ColumnInfo
    val v: String
)