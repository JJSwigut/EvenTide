package com.jjswigut.eventide.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jjswigut.eventide.databinding.FragmentViewpagerBinding


class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewpagerBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewpagerBinding.inflate(inflater, container, false)
        val view = binding.root
        val sectionsPagerAdapter = TabAdapter(requireContext(), childFragmentManager, totalTabs = 3)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)?.setIcon(com.jjswigut.eventide.R.drawable.ic_search)
        tabs.getTabAt(1)?.setIcon(com.jjswigut.eventide.R.drawable.ic_map)
        tabs.getTabAt(2)?.setIcon(com.jjswigut.eventide.R.drawable.ic_tides)
        return view

    }

}