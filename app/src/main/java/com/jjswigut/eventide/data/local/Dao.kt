package com.jjswigut.eventide.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jjswigut.eventide.data.entities.TidalStation

import com.jjswigut.eventide.data.entities.Tide

@Dao
interface Dao {

    @Query("SELECT * FROM station_table")
    fun  getStations() : LiveData<List<TidalStation>>

    @Query("SELECT * FROM tide_table")
    fun getTides() : LiveData<List<Tide>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tides: List<Tide>,stations : List<TidalStation>)




}