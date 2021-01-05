package com.jjswigut.eventide.ui.tides


import android.content.ContentValues.TAG
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TidesFragment : BaseFragment() {

    private var _binding: FragmentTidesBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdapter: TidesListAdapter


    private val viewModel: TideViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = TidesListAdapter(viewModel)

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

    override fun onResume() {
        super.onResume()
        getTides(viewModel.prefs.nearestStationId!!)
        observeAndSortTides()
        observeSortedTidesAndUpdate()
    }

    private fun observeSortedTidesAndUpdate() {
        viewModel.sortedTidesLiveData.observe(viewLifecycleOwner, Observer { list ->
            if (!list.isNullOrEmpty()) {
                binding.stationHeader.text = viewModel.prefs.nearestStationName
                listAdapter.updateData(list)
            }
        })
    }

    private fun getTides(station: String) {
        viewModel.getTidesWithLocation(station)
            .observe(viewLifecycleOwner, Observer {
                if (!it.data.isNullOrEmpty())
                    viewModel.tidesLiveData.value = it.data
            })
    }

    private fun observeAndSortTides() {
        viewModel.tidesLiveData
            .observe(viewLifecycleOwner, Observer {
                if (!it.isNullOrEmpty()) {
                    viewModel.sortedTidesLiveData.value = viewModel.sortTides(it)
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
