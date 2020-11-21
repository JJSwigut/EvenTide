package com.jjswigut.eventide.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tide_table")
data class Extreme(
    @ColumnInfo
    val date: String,
    @PrimaryKey
    val dt: Int,
    @ColumnInfo
    val height: Double,
    @ColumnInfo
    val type: String
)