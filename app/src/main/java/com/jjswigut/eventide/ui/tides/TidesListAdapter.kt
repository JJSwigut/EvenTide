package com.jjswigut.eventide.ui.tides

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jjswigut.eventide.R
import com.jjswigut.eventide.data.entities.Extreme
import com.jjswigut.eventide.databinding.ItemTideBinding
import com.jjswigut.eventide.utils.ListDiffCallback

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1


class TidesListAdapter : RecyclerView.Adapter<TidesListAdapter.ViewHolder>() {


    private val elements: ArrayList<Extreme> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TidesListAdapter.ViewHolder {
        return ViewHolder(
            binding = ItemTideBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            elements = elements
        )
    }

    override fun onBindViewHolder(holder: TidesListAdapter.ViewHolder, position: Int) {
        val item = (elements[position])
        holder.bind(item)
    }

    override fun getItemCount(): Int = elements.size

    fun updateData(newData: List<Extreme>) {
        val diffResult = DiffUtil.calculateDiff(
            ListDiffCallback(newList = newData, oldList = elements)
        )
        elements.clear()
        elements.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }


    class DayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): DayViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.day_header, parent, false)
                return DayViewHolder(view)
            }
        }
    }

    inner class ViewHolder(
        private val binding: ItemTideBinding,
        private val elements: List<Extreme>
    ) : RecyclerView.ViewHolder(binding.root) {

        private val highLowView: TextView = binding.tideHighLow
        private val timeView: TextView = binding.tideTime
        private val heightView: TextView = binding.tideHeight
        private fun element() = elements[adapterPosition]


        fun bind(item: Extreme) {
            highLowView.text = element().type
            timeView.text = element().dt.toString()
            heightView.text = element().height.toString()
        }
    }

    sealed class DataItem {
        abstract val id: Long

        data class TideItem(val tide: Extreme) : DataItem() {
            override val id: Long = tide.id

        }

        data class Day(val day: String) : DataItem() {
            override val id: Long = day.hashCode().toLong()

        }
    }
}
