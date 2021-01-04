package com.jjswigut.eventide.ui.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class TideStationMarker(
    lat: Double,
    lng: Double,
    private val snippet: String?,
    private val title: String?,
    private val id: String
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

    fun getId(): String {
        return id
    }

}