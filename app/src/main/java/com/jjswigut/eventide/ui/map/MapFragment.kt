package com.jjswigut.eventide.ui.map


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.jjswigut.eventide.R
import com.jjswigut.eventide.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : BaseFragment() {

    private val REQUEST_LOCATION_PERMISSION = 1

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        try {
            MapsInitializer.initialize(activity)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        mapView.getMapAsync(OnMapReadyCallback { mapView ->
            googleMap = mapView

        })
        return view
    }

    private fun isPermissionGranted(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }
}
//fun onCreateView(
//    inflater: LayoutInflater, container: ViewGroup?,
//    savedInstanceState: Bundle?
//): View? {
//    val view: View = inflater.inflate(R.layout.fragment_tab2, null, false)
//    mMapView = view.findViewById<View>(R.id.mapView) as MapView
//    mMapView.onCreate(savedInstanceState)
//    mMapView.onResume()
//    try {
//        MapsInitializer.initialize(getActivity().getApplicationContext())
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    mMapView.getMapAsync(OnMapReadyCallback { mMap ->
//        googleMap = mMap
//        googleMap.setMyLocationEnabled(true)
//        //To add marker
//        val sydney = LatLng(-34, 151)
//        googleMap.addMarker(
//            MarkerOptions().position(sydney).title("Title").snippet("Marker Description")
//        )
//        // For zooming functionality
//        val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//    })
//    return view
//}