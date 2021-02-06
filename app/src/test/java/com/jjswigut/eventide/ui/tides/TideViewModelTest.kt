package com.jjswigut.eventide.ui.tides

import com.jjswigut.eventide.data.entities.UIModel
import com.jjswigut.eventide.data.entities.tidalpredictions.Prediction
import com.jjswigut.eventide.data.repository.TideRepository
import com.jjswigut.eventide.utils.Preferences
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TideViewModelTest {

    private lateinit var tideViewModel: TideViewModel

    private val tideRepository = mockk<TideRepository>()
    private val prefs = mockk<Preferences>()

    @Before
    fun setUp() {
        tideViewModel = TideViewModel(tideRepository, prefs)
    }

    // There are usually 2 instances of low tide and 2 of high tide per day. When given
    // a list of of predictions, viewModel.sortTides groups them by day to create a DayHeader
    // for each day's tides. This test confirms that the DayHeaders are created and inserted
    // properly into the list.
    @Test
    fun `when given a list of predictions then UIModel is properly made`() {
        var modelList = arrayListOf<UIModel>()
        val list = arrayListOf<Prediction>()
        for (i in 0..9) {
            list.add(Prediction(t = "2021-01-0$i 4:24", v = "-3.${i}m", type = "Low"))
        }
        for (i in 0..9) {
            list.add(Prediction(t = "2021-01-0$i 12:22", v = "2.${i}m", type = "High"))
        }
        for (i in 0..9) {
            list.add(Prediction(t = "2021-01-0$i 3:26", v = "-1.${i}m", type = "Low"))
        }
        for (i in 0..9) {
            list.add(Prediction(t = "2021-01-0$i 12:23", v = "4.${i}m", type = "High"))
        }

        modelList = tideViewModel.sortTides(list)
        val dayHeader1 = modelList[0] as UIModel.DayModel
        val dayHeader2 = modelList[5] as UIModel.DayModel
        val dayHeader3 = modelList[10] as UIModel.DayModel
        val dayHeader4 = modelList[15] as UIModel.DayModel

        assertEquals(dayHeader1.dayHeader.day, "2021-01-00")
        assertEquals(dayHeader2.dayHeader.day, "2021-01-01")
        assertEquals(dayHeader3.dayHeader.day, "2021-01-02")
        assertEquals(dayHeader4.dayHeader.day, "2021-01-03")
    }
}