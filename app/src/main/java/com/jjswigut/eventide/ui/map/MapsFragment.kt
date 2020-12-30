package com.jjswigut.eventide.ui.map

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jjswigut.eventide.R
import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStation
import com.jjswigut.eventide.databinding.FragmentMapsBinding
import com.jjswigut.eventide.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapsFragment : BaseFragment() {

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var map: GoogleMap
    private lateinit var listAdapter: MapCardAdapter
    private val viewModel: MapViewModel by activityViewModels()
    private var stationList = arrayListOf<PredictionStation>()
    private var markerList = arrayListOf<Marker>()

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!


    private val mapStart = OnMapReadyCallback { googleMap ->
        map = googleMap
        val zoom = 10f
        val location = viewModel.prefs.userLocation
        val youAreHere = googleMap.addMarker(
            MarkerOptions().position(location).title("You are here!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        )
        youAreHere.showInfoWindow()

        stationList.forEach { station ->
            makeMarker(map, station)
            markerList.add(makeMarker(map, station))
        }

        googleMap.setOnInfoWindowClickListener { marker ->
            val stationId = marker.tag.toString().filter { it.isDigit() }
            val url = "https://tidesandcurrents.noaa.gov/noaatidepredictions.html?id=$stationId"
            launchCustomTab(url)
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
        enableMyLocation()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listAdapter = MapCardAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getAndObserveStations()
    }

    override fun onResume() {
        super.onResume()
        observeStationClick()
    }

    private fun getAndObserveStations() {
        viewModel.stationLiveData
            .observe(viewLifecycleOwner, Observer {
                if (!it.data.isNullOrEmpty())
                    stationList = viewModel.buildStationList(it.data)
                updateMap()
            })
    }


    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun updateMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(mapStart)
    }

    private fun updateMapFromStationList(location: PredictionStation) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(goToMapWithStation(location))
        listAdapter.updateData(viewModel.sortTidesForMapCards(viewModel.tidesLiveData.value))
    }

    private fun goToMapWithStation(location: PredictionStation): OnMapReadyCallback {
        return OnMapReadyCallback { googleMap ->
            map = googleMap
            val zoom = 10f
            val latLng = LatLng(location.lat, location.lng)
            markerList.forEach { if (it.tag == location.id) it.showInfoWindow() }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
            enableMyLocation()


        }
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

    private fun observeStationClick() {
        viewModel.stationClicked.observe(viewLifecycleOwner, Observer { stationClicked ->
            if (stationClicked) {
                viewModel.tidesLiveData.observe(viewLifecycleOwner, Observer {
                    if (!it.isNullOrEmpty()) {
                        listAdapter.updateData(viewModel.sortTidesForMapCards(it))
                    }
                })
                viewModel.station?.let { station -> updateMapFromStationList(station) }
            } else updateMap()
        })
    }

    private fun makeMarker(map: GoogleMap, station: PredictionStation): Marker {
        val marker = map.addMarker(
            MarkerOptions().position(LatLng(station.lat, station.lng))
                .title(station.name)
        )
        marker.tag = station.id
        return marker
    }

    private fun setupRecyclerView() {
        binding.mapRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
        binding.mapRecyclerView.adapter = listAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}