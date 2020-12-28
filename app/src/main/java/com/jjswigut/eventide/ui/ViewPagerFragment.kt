package com.jjswigut.eventide.ui


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.jjswigut.eventide.R
import com.jjswigut.eventide.databinding.FragmentViewpagerBinding
import com.jjswigut.eventide.ui.search.StationViewModel
import javax.inject.Singleton

@Singleton
class ViewPagerFragment : Fragment() {
    private lateinit var viewPager: ViewPager
    private val viewModel: StationViewModel by activityViewModels()
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
        viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)?.setIcon(com.jjswigut.eventide.R.drawable.ic_map)
        tabs.getTabAt(1)?.setIcon(com.jjswigut.eventide.R.drawable.ic_station)
        tabs.getTabAt(2)?.setIcon(com.jjswigut.eventide.R.drawable.ic_tides)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)

    }

    override fun onResume() {
        super.onResume()
        setTheme()

    }

    private fun setTheme() {
        if (viewModel.preferences.darkMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getFragment(item: Int) {
        viewPager.setCurrentItem(item, true)
    }


}