package com.jjswigut.eventide.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tide_table")
data class Tide(
    @PrimaryKey
    var dt:Long,
    @ColumnInfo
    var date:String,
    @ColumnInfo
    var height:Double,
    @ColumnInfo
    var type:String
){


}
//"dt":1603067148,
//"date":"2020-10-19T00:25+0000",
//"height":-1.013,
//"type":"Low"
