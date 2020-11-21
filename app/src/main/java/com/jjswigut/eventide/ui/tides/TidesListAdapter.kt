package com.jjswigut.eventide.ui.tides

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jjswigut.eventide.R
import com.jjswigut.eventide.data.entities.UIModel
import com.jjswigut.eventide.databinding.DayHeaderBinding
import com.jjswigut.eventide.databinding.ItemTideBinding


class TidesListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var elements: ArrayList<UIModel> = ArrayList()

    fun submitData(list: ArrayList<UIModel>) {
        elements.clear()
        elements.addAll(list)
        Log.d(TAG, "submitData: $list ")
    }

    override fun getItemCount(): Int = elements.size

    override fun getItemViewType(position: Int) =
        when (elements[position]) {
            is UIModel.DayModel -> R.layout.day_header
            is UIModel.TideModel -> R.layout.item_tide
            null -> throw IllegalArgumentException("Unknown View")
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            R.layout.day_header -> DayViewHolder(
                binding = DayHeaderBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            R.layout.item_tide -> TideViewHolder(
                binding = ItemTideBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> throw Exception("Nope")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = elements[position]
        when (holder) {
            is DayViewHolder -> holder.bind(item as UIModel.DayModel)
            is TideViewHolder -> holder.bind(item as UIModel.TideModel)
        }
    }

    inner class TideViewHolder(
        private val binding: ItemTideBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val highLowView: TextView = binding.tideHighLow
        private val timeView: TextView = binding.tideTime
        private val heightView: TextView = binding.tideHeight


        fun bind(item: UIModel.TideModel) {
            highLowView.text = item.tideItem.type
            timeView.text = item.tideItem.dt.toString()
            heightView.text = item.tideItem.height.toString()
        }
    }

    inner class DayViewHolder(
        private val binding: DayHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dayView: TextView = binding.dayHeader

        fun bind(item: UIModel.DayModel) {
            dayView.text = item.dayHeader.day
        }

    }


}



