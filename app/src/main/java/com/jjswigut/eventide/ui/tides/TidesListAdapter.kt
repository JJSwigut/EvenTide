package com.jjswigut.eventide.ui.tides

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jjswigut.eventide.data.entities.Extreme
import com.jjswigut.eventide.databinding.DayHeaderBinding
import com.jjswigut.eventide.databinding.ItemTideBinding
import com.jjswigut.eventide.utils.ListDiffCallback

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1


class TidesListAdapter : RecyclerView.Adapter<TidesListAdapter.ViewHolder>() {


    private val elements: ArrayList<DataItem> = arrayListOf()


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

    override fun getItemViewType(position: Int) = when (elements[position]) {
        is DataItem.TideItem -> ITEM_VIEW_TYPE_ITEM
        is DataItem.Day -> ITEM_VIEW_TYPE_HEADER
    }

    override fun onBindViewHolder(holder: TidesListAdapter.ViewHolder, position: Int) {
        val item = (elements[position])
        holder.bind(item)
    }

    override fun getItemCount(): Int = elements.size

    fun updateData(newData: List<DataItem>) {
        val diffResult = DiffUtil.calculateDiff(
            ListDiffCallback(newList = newData, oldList = elements)
        )
        elements.clear()
        elements.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }


    class DayViewHolder(private val binding: DayHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(day: String) {
            binding.dayHeader.text = day
        }

        companion object {
            fun from(parent: ViewGroup): DayViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DayHeaderBinding.inflate(layoutInflater, parent, false)
                return DayViewHolder(binding)
            }
        }
    }

    inner class ViewHolder(
        private val binding: ItemTideBinding,
        private val elements: List<DataItem>
    ) : RecyclerView.ViewHolder(binding.root) {

        private val highLowView: TextView = binding.tideHighLow
        private val timeView: TextView = binding.tideTime
        private val heightView: TextView = binding.tideHeight
        private fun element(): DataItem = elements[adapterPosition]


        fun bind(item: DataItem) {
            highLowView.text = element().tide.type
            timeView.text = element().tide.dt.toString()
            heightView.text = element().tide.height.toString()
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
