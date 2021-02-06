package com.jjswigut.eventide.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.jjswigut.eventide.data.entities.tidalpredictions.Prediction
import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class AppDatabaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var dao: Dao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.dao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertTides() = runBlockingTest {
        val listOfTides = arrayListOf<Prediction>()
        for (i in 0..9) {
            listOfTides.add(Prediction("12:0$i", "High", "1.8m"))
        }
        dao.insertTides(listOfTides)

        val databaseTides = dao.getTides().getOrAwaitValue()

        assertThat(databaseTides).containsExactlyElementsIn(listOfTides)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertStations() = runBlockingTest {
        val listOfStations = arrayListOf<PredictionStation>()
        for (i in 0..9) {
            listOfStations.add(PredictionStation("294356$i", "CT", 74.34, -120.32, "Station $i"))
        }
        dao.insertStations(listOfStations)

        val databaseStations: List<PredictionStation> =
            dao.getPredictionStations().getOrAwaitValue()

        assertThat(databaseStations).containsExactlyElementsIn(listOfStations)
    }
}
