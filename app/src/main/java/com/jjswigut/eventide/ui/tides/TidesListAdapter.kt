package com.jjswigut.eventide.ui.tides

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jjswigut.eventide.R
import com.jjswigut.eventide.data.entities.UIModel
import com.jjswigut.eventide.databinding.DayHeaderBinding
import com.jjswigut.eventide.databinding.ItemTideBinding
import com.jjswigut.eventide.utils.ListDiffCallback
import com.jjswigut.eventide.utils.Preferences
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TidesListAdapter(private val viewModel: TideViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var elements: ArrayList<UIModel> = ArrayList()

    fun updateData(newData: List<UIModel>) {

        val diffResult = DiffUtil.calculateDiff(
            ListDiffCallback(newList = newData, oldList = elements)
        )
        elements.clear()
        elements.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun getItemCount(): Int = elements.size

    override fun getItemViewType(position: Int) =
        when (elements[position]) {
            is UIModel.DayModel -> R.layout.day_header
            is UIModel.TideModel -> R.layout.item_tide
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
                    ),
                    parent, false
                ),
                prefs = viewModel.prefs
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
        binding: ItemTideBinding,
        private val prefs: Preferences
    ) : RecyclerView.ViewHolder(binding.root) {


        private val highLowView: TextView = binding.tideHighLow
        private val timeView: TextView = binding.tideTime
        private val heightView: TextView = binding.tideHeight


        fun bind(item: UIModel.TideModel) {
            highLowView.text = highLowElaborator(item.tideItem.type)
            timeView.text = timeFormatter(item.tideItem.t)
            heightView.text = heightString(item.tideItem.v.toDouble())

        }

        private fun timeFormatter(date: String): String {
            val fullDate = date.take(16)
            val time = fullDate.takeLast(5)
            var hour = time.take(2).toInt()
            var timeStamp: String
            val timeString: String

            timeStamp = if (hour >= 12) {
                " pm"
            } else " am"

            if (hour > 12) {
                hour -= 12
            } else if (hour == 0) {
                hour = 12
                timeStamp = " am"
            }
            timeString = if (hour < 10) {
                hour.toString().takeLast(1) + time.takeLast(3)
            } else hour.toString().takeLast(2) + time.takeLast(3)

            return timeString + timeStamp
        }

        private fun heightString(height: Double): String {
            return if (prefs.units) {
                val heightInFeet = (height * 3.28084)
                String.format("%.2f ft", heightInFeet)
            } else String.format("%.2f m", height)
        }

        private fun highLowElaborator(type: String): String {
            if (type.contentEquals("L")) {
                return "Low"
            } else return "High"

        }
    }


    inner class DayViewHolder(
        binding: DayHeaderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dayView: TextView = binding.dayHeader

        fun bind(item: UIModel.DayModel) {
            dayView.text = dayFormatter(item.dayHeader.day)
        }

        private fun dayFormatter(date: String): String {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val formatter = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.US)
            val parsedDate = parser.parse(date)
            return formatter.format(parsedDate!!)
        }
//2021-01-16 10:06
    }


}



