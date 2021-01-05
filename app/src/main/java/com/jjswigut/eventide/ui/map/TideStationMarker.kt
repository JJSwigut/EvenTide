package com.jjswigut.eventide.ui.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class TideStationMarker(
    val lat: Double,
    val lng: Double,
    private val snippet: String?,
    private val title: String?,
    val id: String
) : ClusterItem {

    private val position: LatLng = LatLng(lat, lng)


    override fun getSnippet(): String? {
        return snippet
    }

    override fun getTitle(): String? {
        return title
    }


    override fun getPosition(): LatLng {
        return position
    }


}