package com.jjswigut.eventide.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jjswigut.eventide.ui.map.MapFragment
import com.jjswigut.eventide.ui.search.SearchFragment
import com.jjswigut.eventide.ui.tides.TidesFragment

@Suppress("DEPRECATION")
class TabAdapter internal constructor(
    var context: Context,
    fm: FragmentManager?,
    var totalTabs:Int) :
    FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                SearchFragment()
            }
            1 -> {
                MapFragment()
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