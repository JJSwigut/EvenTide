package com.jjswigut.eventide.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jjswigut.eventide.data.entities.TidalStation
import com.jjswigut.eventide.databinding.ItemStationBinding
import com.jjswigut.eventide.ui.StationAction
import com.jjswigut.eventide.utils.ListDiffCallback


class StationListAdapter(private val actionHandler: StationActionHandler) :
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
            elements = elements
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
        private val elements: List<TidalStation>
    ) : RecyclerView.ViewHolder(binding.root) {

        private val nameView: TextView = binding.stationNameView
        private val distanceView: TextView = binding.stationDistanceView
        fun element() = elements[adapterPosition]


        fun bind(item: TidalStation) {

            nameView.text = element().name
            distanceView.text = element().id
            binding.stationView.setOnClickListener {
                actionHandler(StationAction.StationClicked(position, item))
            }
        }
    }


}

typealias StationActionHandler = (StationAction.StationClicked) -> Unit
