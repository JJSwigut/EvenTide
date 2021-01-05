package com.jjswigut.eventide.ui.stations


import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.jjswigut.eventide.R
import com.jjswigut.eventide.databinding.FragmentStationBinding
import com.jjswigut.eventide.ui.BaseFragment
import com.jjswigut.eventide.ui.map.MapViewModel
import com.jjswigut.eventide.ui.stations.StationAction.StationClicked
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_viewpager.*

@AndroidEntryPoint
class StationFragment : BaseFragment() {

    private val REQUEST_LOCATION_PERMISSION = 1

    private var _binding: FragmentStationBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: StationListAdapter

    private val viewModel: StationViewModel by activityViewModels()
    private val mapViewModel: MapViewModel by activityViewModels()


    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
        listAdapter = StationListAdapter(
            ::handleAction,
            viewModel.preferences
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getAndObserveStations()

        Log.d(TAG, "onViewCreated: RecyclerView Set up")
    }




    private fun handleAction(action: StationAction) {
        updateMapViewModelWithStationInfo(action)
        navigateToTab(0)
    }


    private fun getAndObserveStations() {
        viewModel.getPredictionStations()
            .observe(viewLifecycleOwner, Observer {
                if (!it.data.isNullOrEmpty())
                    viewModel.stationLiveData.value = it.data
                val sordidStations =
                    viewModel.sortStationsByDistance(viewModel.preferences.userLocation)
                if (sordidStations != null) {
                    listAdapter.updateData(sordidStations)
                }
            })
    }


    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = listAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLastLocation()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_location_explanation),
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }

        }
    }

    private fun updateMapViewModelWithStationInfo(action: StationAction) {
        when (action) {
            is StationClicked -> {
                mapViewModel.station = action.station
                mapViewModel.stationClicked.value = true
                mapViewModel.getTidesWithLocation(action.station.id)
                    .observe(viewLifecycleOwner, Observer {
                        if (!it.data.isNullOrEmpty())
                            mapViewModel.tidesLiveData.value = it.data
                    })
                Log.d(
                    TAG,
                    "updateMapViewModelWithStationInfo: ${mapViewModel.tidesLiveData.value} "
                )
            }
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_LOCATION_PERMISSION
        )
    }

    private fun getLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (getLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    viewModel.preferences.saveLocation(location)
                    viewModel.userLocation.value = LatLng(location.latitude, location.longitude)
                } else {
                    requestCurrentLocation()
                }
            }
        } else requestLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun requestCurrentLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 20 * 1000
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (locationResult != null && locationResult.locations.isNotEmpty()) {
                    locationResult.locations.firstOrNull()?.let {
                        viewModel.preferences.saveLocation(it)
                        viewModel.userLocation.value = LatLng(it.latitude, it.longitude)
                    }

                } else Toast.makeText(
                    requireContext(),
                    getString(R.string.location_not_working),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun navigateToTab(tab: Int) {
        requireParentFragment().view_pager.currentItem = tab
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}