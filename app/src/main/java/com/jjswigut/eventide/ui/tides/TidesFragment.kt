package com.jjswigut.eventide.ui.tides


import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jjswigut.eventide.databinding.FragmentTidesBinding
import com.jjswigut.eventide.ui.BaseFragment
import com.jjswigut.eventide.ui.search.SearchFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TidesFragment : BaseFragment(), OnSharedPreferenceChangeListener {

    private var _binding: FragmentTidesBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: TidesListAdapter


    private val viewModel: SearchFragmentViewModel by activityViewModels()
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = TidesListAdapter(viewModel.preferences)

        prefs = viewModel.preferences.prefs

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTidesBinding.inflate(inflater, container, false)
        val view = binding.root
        setupRecyclerView()
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()

    }

    override fun onResume() {
        super.onResume()
        prefs.registerOnSharedPreferenceChangeListener(this)

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        listAdapter.updateData(viewModel.sortedTidesLiveData.value!!)
    }

    override fun onPause() {
        super.onPause()
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun setupObservers() {
        viewModel.userLocation.observe(viewLifecycleOwner, Observer {
            if (it != null) getAndObserveTides(it)
        })
        viewModel.tidesLiveData.observe(viewLifecycleOwner, Observer { list ->
            if (!list.isNullOrEmpty()) {
                listAdapter.updateData(viewModel.sortTides(list))
            }
        })
    }

    private fun getAndObserveTides(it: Location) {
        viewModel.getTidesWithLocation(viewModel.userLocation.value!!)
            .observe(viewLifecycleOwner, Observer {
                if (!it.data.isNullOrEmpty()) {
                    viewModel.tidesLiveData.value = it.data
                    Log.d(TAG, "getAndObserveTides: tides observed")
                }
            })
    }

    private fun setupRecyclerView() {
        binding.tideRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.tideRecyclerView.adapter = listAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}
