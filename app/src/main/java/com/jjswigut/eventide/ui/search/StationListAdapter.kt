package com.jjswigut.eventide.ui.search

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jjswigut.eventide.data.entities.TidalStation
import com.jjswigut.eventide.databinding.ItemStationBinding
import com.jjswigut.eventide.ui.StationAction
import com.jjswigut.eventide.utils.ListDiffCallback
import com.jjswigut.eventide.utils.Preferences


class StationListAdapter(
    private val actionHandler: StationActionHandler,
    private val userLocation: Location,
    private val prefs: Preferences
) :
    RecyclerView.Adapter<StationListAdapter.ViewHolder>() {


    private val elements: ArrayList<TidalStation> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = ItemStationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            actionHandler = actionHandler,
            elements = elements,
            prefs = prefs
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = (elements[position])
        holder.bind(item)
    }

    override fun getItemCount(): Int = elements.size


    fun updateData(newData: List<TidalStation>) {

        val diffResult = DiffUtil.calculateDiff(
            ListDiffCallback(newList = newData, oldList = elements)
        )
        elements.clear()
        elements.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }


    inner class ViewHolder(
        private val binding: ItemStationBinding,
        private val actionHandler: StationActionHandler,
        private val elements: List<TidalStation>,
        private val prefs: Preferences
    ) : RecyclerView.ViewHolder(binding.root) {

        private val nameView: TextView = binding.stationNameView
        private val distanceView: TextView = binding.stationDistanceView
        private fun element() = elements[adapterPosition]


        fun bind(item: TidalStation) {

            nameView.text = element().name
            distanceView.text = distance()
            binding.stationView.setOnClickListener {
                actionHandler(StationAction.StationClicked(position, item))
            }
        }

        private fun distance(): String {
            val stationLocation = Location("")
            stationLocation.latitude = element().lat
            stationLocation.longitude = element().lon
            val distance = userLocation
                .distanceTo(stationLocation).div(1000)
                .toString().take(4)
            return if (prefs.units) {
                val distanceInMiles = (distance.toDouble() * 0.6214)
                String.format("%.2f mi", distanceInMiles)
            } else ("$distance km")
        }
    }


}

typealias StationActionHandler = (StationAction.StationClicked) -> Unit
