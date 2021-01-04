package com.jjswigut.eventide.ui.map

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
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

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var clusterManager: ClusterManager<TideStationMarker>

    private lateinit var renderer: DefaultClusterRenderer<TideStationMarker>


    private val mapStart = OnMapReadyCallback { googleMap ->

        map = googleMap
        map.clear()
        val zoom = 3f
        val location = viewModel.prefs.userLocation
        val youAreHere = googleMap.addMarker(
            MarkerOptions().position(location).title("You are here!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
        )
        youAreHere.showInfoWindow()

        addClusters(map)

        googleMap.uiSettings.isMapToolbarEnabled = false

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
        binding.recyclerExit.setOnClickListener {
            binding.mapRecyclerView.visibility = View.GONE
            binding.recyclerExit.visibility = View.GONE
        }
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
                updateMap(mapStart)
            })
    }

    private fun addClusters(map: GoogleMap) {
        clusterManager = ClusterManager(requireContext(), map)
        renderer = DefaultClusterRenderer(requireContext(), map, clusterManager)
        clusterManager.setOnClusterClickListener {
            val zoom = map.cameraPosition.zoom + 2f
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(it.position, zoom))
            return@setOnClusterClickListener true
        }
        clusterManager.setOnClusterItemClickListener {
            getTidesFromMarkerClick(it)
            observeTides()
            return@setOnClusterItemClickListener false
        }

        clusterManager.setOnClusterItemInfoWindowClickListener {
            val url = "https://tidesandcurrents.noaa.gov/noaatidepredictions.html?id=${it.getId()}"
            launchCustomTab(url)
        }

        map.setOnCameraIdleListener(clusterManager)
        map.setOnMarkerClickListener(clusterManager)

        stationList.forEach { marker ->
            clusterManager.addItem(
                TideStationMarker(
                    marker.lat,
                    marker.lng,
                    marker.state,
                    marker.name,
                    marker.id
                )
            )
        }

        Log.d(TAG, "addClusters: ${clusterManager.markerCollection.markers.size}")

        clusterManager.setAnimation(true)

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
        }
    }

    private fun updateMap(callback: OnMapReadyCallback) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }


    private fun updateRecyclerView() {
        listAdapter.updateData(viewModel.sortTidesForMapCards(viewModel.tidesLiveData.value))
        binding.mapRecyclerView.visibility = View.VISIBLE
        binding.recyclerExit.visibility = View.VISIBLE
    }

    private fun fromStationList(location: PredictionStation): OnMapReadyCallback {
        return OnMapReadyCallback { googleMap ->
            map = googleMap
            val zoom = 12f
            val latLng = LatLng(location.lat, location.lng)

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
                observeTides()
                viewModel.station?.let { station ->
                    updateMap(fromStationList(station))
                }
                updateRecyclerView()
            }
        })
    }

    private fun observeTides() {
        viewModel.tidesLiveData.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                updateRecyclerView()
            }
        })
    }

    private fun getTidesFromMarkerClick(marker: TideStationMarker) {
        marker.getId().let { id ->
            viewModel.getTidesWithLocation(id)
                .observe(viewLifecycleOwner, Observer {
                    if (!it.data.isNullOrEmpty()) {
                        viewModel.tidesLiveData.value = it.data
                    }
                })
        }
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