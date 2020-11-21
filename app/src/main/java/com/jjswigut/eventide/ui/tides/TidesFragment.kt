package com.jjswigut.eventide.ui.tides


import android.content.ContentValues.TAG
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jjswigut.eventide.data.entities.DayHeader
import com.jjswigut.eventide.data.entities.UIModel
import com.jjswigut.eventide.databinding.FragmentTidesBinding
import com.jjswigut.eventide.ui.BaseFragment
import com.jjswigut.eventide.ui.search.SearchFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TidesFragment : BaseFragment() {

    private var _binding: FragmentTidesBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: TidesListAdapter

    private val viewModel: SearchFragmentViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = TidesListAdapter()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTidesBinding.inflate(inflater, container, false)
        val view = binding.root

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setupObservers()

    }

    private fun setupObservers() {
        viewModel.userLocation.observe(viewLifecycleOwner, Observer {
            if (it != null) getAndObserveTides(it)
        })
        viewModel.tidesLiveData.observe(viewLifecycleOwner, Observer { list ->
            if (!list.isNullOrEmpty()) {
                var sordidTides = list.groupBy { it.date.take(10) }
                var flatTides = sordidTides.toList()

                Log.d(TAG, "setupObservers: $flatTides")
                //listAdapter.submitData(flatTides as ArrayList<UIModel>)
                val uiModels = arrayListOf<UIModel>()
                flatTides.forEach { pair ->
                    uiModels.add(UIModel.DayModel(DayHeader(pair.first)))
                    pair.second.forEach { extreme ->
                        uiModels.add(UIModel.TideModel(extreme))
                    }
                }
                listAdapter.submitData(uiModels)

            }

        })
    }
//    sordidTides.flatMap { date -> mutableListOf<Any>(date.key).also { it.addAll(date.value) } }
//

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
