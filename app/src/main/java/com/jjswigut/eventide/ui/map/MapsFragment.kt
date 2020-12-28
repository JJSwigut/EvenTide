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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jjswigut.eventide.R
import com.jjswigut.eventide.data.entities.tidalpredictions.PredictionStation
import com.jjswigut.eventide.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapsFragment : BaseFragment() {

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var map: GoogleMap
    private val viewModel: MapViewModel by activityViewModels()
    private var stationList = arrayListOf<PredictionStation>()


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
            googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(station.lat, station.lng))
                    .title(station.name)
            ).tag = station.id

        }

        googleMap.setOnInfoWindowClickListener { marker ->
            val stationId = marker.tag.toString().filter { it.isDigit() }
            val url = "https://tidesandcurrents.noaa.gov/noaatidepredictions.html?id=$stationId"
            launchCustomTab(url)
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
        enableMyLocation()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAndObserveStations()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.stationClicked) {
            viewModel.stationLatLng?.let { updateMapFromStationList(it) }
        }
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

    private fun updateMapFromStationList(location: LatLng) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(goToMapWithStation(location))
    }

    private fun goToMapWithStation(location: LatLng): OnMapReadyCallback {
        return OnMapReadyCallback { googleMap ->
            map = googleMap
            val zoom = 10f
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
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

}