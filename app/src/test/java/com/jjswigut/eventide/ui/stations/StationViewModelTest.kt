package com.jjswigut.eventide.ui.stations

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStation
import com.jjswigut.eventide.data.repository.StationRepository
import com.jjswigut.eventide.utils.Preferences
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class StationViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var stationViewModel: StationViewModel

    private val repo = mockk<StationRepository>()
    private val prefs = mockk<Preferences>()


    @Before
    fun setUp() {
        stationViewModel = StationViewModel(repo, prefs)
    }

    @After
    fun tearDown() {
    }

    @Test

    fun `when given user location we get a list of stations sorted by distance`() {

        val list = arrayListOf<PredictionStation>()
        val user = LatLng(34.00, -76.00)
        for (i in 0..9) {
            list.add(
                PredictionStation(
                    id = "167348$i",
                    state = "CT",
                    lat = 34.25 + i,
                    lng = -76.25 - i,
                    name = "Station$i"
                )
            )
        }

        stationViewModel.stationLiveData.value = list
        stationViewModel.sortStationsByDistance(user)

        list.forEach {
            assertTrue(distance(list[0], user) <= distance(it, user))
        }

    }

    private fun distance(station: PredictionStation, user: LatLng): Float {
        val stationLocation = Location("")
        stationLocation.latitude = station.lat
        stationLocation.longitude = station.lng
        val userLocation = Location("")
        userLocation.latitude = user.latitude
        userLocation.longitude = user.longitude
        return userLocation
            .distanceTo(stationLocation)
    }
}