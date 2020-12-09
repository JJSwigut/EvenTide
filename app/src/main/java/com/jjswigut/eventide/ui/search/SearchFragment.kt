package com.jjswigut.eventide.ui.search


import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jjswigut.eventide.R
import com.jjswigut.eventide.databinding.FragmentSearchBinding
import com.jjswigut.eventide.ui.BaseFragment
import com.jjswigut.eventide.ui.SharedViewModel
import com.jjswigut.eventide.ui.search.StationAction.StationClicked
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment() {

    private val REQUEST_LOCATION_PERMISSION = 1

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: StationListAdapter

    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
        Log.d(TAG, "onCreate: getting location")
        if (getLastLocation()) {
            listAdapter = StationListAdapter(
                ::handleAction,
                viewModel.preferences
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        Log.d(TAG, "onViewCreated: RecyclerView Set up")
    }


    override fun onResume() {
        super.onResume()
        setupObservers()
    }


    private fun handleAction(action: StationAction) {
        when (action) {
            is StationClicked -> {
                view?.let {
                    val stationId = action.station.id.filter { it.isDigit() }
                    val url = "https://www.tidesandcurrents.noaa.gov/stationhome.html?id=$stationId"
                    launchCustomTab(url)
                }
            }

        }
    }

    private fun setupObservers() {
        viewModel.userLocation.observe(viewLifecycleOwner, Observer {
            if (it != null) getAndObserveStations(it)
        })
    }

    private fun getAndObserveStations(it: Location) {
        viewModel.getStationsWithLocation(it)
            .observe(viewLifecycleOwner, Observer {
                if (!it.data.isNullOrEmpty())
                    listAdapter.updateData(ArrayList(it.data))
                viewModel.stationLiveData.value = it.data
            })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = listAdapter
    }


    //TODO: After getting permission the app needs to try this function again. It currently doesn't.
    private fun getLastLocation(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )

        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                viewModel.userLocation.value = location
                viewModel.preferences.saveLocation(location)
                Log.d(TAG, "getLastLocation: got location")
            }
        }
        return true
    }

    private fun launchCustomTab(url: String) {
        val builder = CustomTabsIntent.Builder()

        builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.primaryLightColor))
        builder.setNavigationBarColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.primaryDarkColor
            )
        )
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}