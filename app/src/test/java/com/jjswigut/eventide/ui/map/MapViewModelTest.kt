package com.jjswigut.eventide.ui.map

import com.jjswigut.eventide.data.entities.tidalpredictions.Prediction
import com.jjswigut.eventide.data.repository.StationRepository
import com.jjswigut.eventide.data.repository.TideRepository
import com.jjswigut.eventide.utils.Preferences
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class MapViewModelTest {

    private lateinit var mapViewModel: MapViewModel

    private val stationRepo = mockk<StationRepository>()
    private val tideRepository = mockk<TideRepository>()
    private val prefs = mockk<Preferences>()

    @Before
    fun setUp() {
        mapViewModel = MapViewModel(stationRepo, tideRepository, prefs)

    }

    @Test
    fun `when I call sortTides I expect the list grouped correctly`() {
        val list = arrayListOf<Prediction>()
        for (i in 0..10) {
            list.add(Prediction(t = "2021-01-0$i 11:24", v = "-3.${i}m", type = "Low"))
        }
        list.add(Prediction(t = "2021-01-03 11:24", v = "-3.2m", type = "Low"))
        list.add(Prediction(t = "2021-01-04 11:24", v = "-3.2m", type = "Low"))
        list.add(Prediction(t = "2021-01-05 11:24", v = "-3.2m", type = "Low"))
        val sortedList = mapViewModel.sortTidesForMapCards(list)

        assertTrue(sortedList.size == 10)
        assertTrue(sortedList[1].list.size == 2)
        assertTrue(sortedList[3].list.size == 2)
        assertTrue(sortedList[4].list.size == 2)
        assertTrue(sortedList[5].list.size == 2)
    }
}