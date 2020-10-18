package com.jjswigut.eventide.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jjswigut.eventide.data.entities.TidalStation
import com.jjswigut.eventide.data.entities.Tide

@Database(entities = [Tide::class,TidalStation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): Dao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "tides_and_stations")
                .fallbackToDestructiveMigration()
                .build()
    }

}