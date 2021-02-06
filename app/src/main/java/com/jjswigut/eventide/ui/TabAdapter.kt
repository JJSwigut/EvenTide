package com.jjswigut.eventide.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jjswigut.eventide.ui.map.MapsFragment
import com.jjswigut.eventide.ui.stations.StationFragment
import com.jjswigut.eventide.ui.tides.TidesFragment

@Suppress("DEPRECATION")
class TabAdapter internal constructor(
    var context: Context,
    fm: FragmentManager?,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MapsFragment()
            }
            1 -> {
                StationFragment()
            }
            2 -> {
                TidesFragment()
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}
