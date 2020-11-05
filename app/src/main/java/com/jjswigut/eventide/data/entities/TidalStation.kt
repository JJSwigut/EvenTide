package com.jjswigut.eventide.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station_table")
class TidalStation(
    @PrimaryKey
    var id: String,
    @ColumnInfo
    var name: String,
    @ColumnInfo
    var lat: Double,
    @ColumnInfo
    var lon: Double
)

//"id":"NOAA_SUB:9410686",
//"name":"Long Beach, Inner Harbor, California",
//"lat":"33.771702",
//"lon":"-118.209999",
//"timezone":"America\/Los_Angeles"