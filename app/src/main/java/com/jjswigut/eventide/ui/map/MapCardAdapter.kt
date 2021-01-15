package com.jjswigut.eventide.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jjswigut.eventide.data.entities.TideCard
import com.jjswigut.eventide.databinding.MapTideCardBinding
import com.jjswigut.eventide.utils.ListDiffCallback
import com.jjswigut.eventide.utils.Preferences
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MapCardAdapter(private val viewModel: MapViewModel) :
    RecyclerView.Adapter<MapCardAdapter.TideCardViewHolder>() {

    private var elements: ArrayList<TideCard> = ArrayList()

    fun updateData(newData: List<TideCard>) {

        val diffResult = DiffUtil.calculateDiff(
            ListDiffCallback(newList = newData, oldList = elements)
        )
        elements.clear()
        elements.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun getItemCount(): Int = elements.size


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MapCardAdapter.TideCardViewHolder {
        return TideCardViewHolder(
            binding = MapTideCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            prefs = viewModel.prefs
        )
    }

    override fun onBindViewHolder(holder: TideCardViewHolder, position: Int) {
        val item = (elements[position])
        holder.bind(item)
    }

    inner class TideCardViewHolder(
        binding: MapTideCardBinding,
        private val prefs: Preferences
    ) : RecyclerView.ViewHolder(binding.root) {


        private val cardDate = binding.tideCardDate

        private val tide1HighLow = binding.tideItem1.mapTideHighLow
        private val tide1Time = binding.tideItem1.mapTideTime
        private val tide1Height = binding.tideItem1.mapTideHeight

        private val tide2HighLow = binding.tideItem2.mapTideHighLow
        private val tide2Time = binding.tideItem2.mapTideTime
        private val tide2Height = binding.tideItem2.mapTideHeight

        private val tide3HighLow = binding.tideItem3.mapTideHighLow
        private val tide3Time = binding.tideItem3.mapTideTime
        private val tide3Height = binding.tideItem3.mapTideHeight

        private val tide4HighLow = binding.tideItem4.mapTideHighLow
        private val tide4Time = binding.tideItem4.mapTideTime
        private val tide4Height = binding.tideItem4.mapTideHeight

        private val tide5HighLow = binding.tideItem5.mapTideHighLow
        private val tide5Time = binding.tideItem5.mapTideTime
        private val tide5Height = binding.tideItem5.mapTideHeight


        fun bind(item: TideCard) {
            cardDate.text = dayFormatter(item.date)

            tide1HighLow.text = highLowElaborator(item.list[0].type)
            tide1Time.text = timeFormatter(item.list[0].t)
            tide1Height.text = heightString(item.list[0].v.toDouble())

            if (item.list.size > 1) {
                tide2HighLow.text = highLowElaborator(item.list[1].type)
                tide2Time.text = timeFormatter(item.list[1].t)
                tide2Height.text = heightString(item.list[1].v.toDouble())
            }

            if (item.list.size > 2) {
                tide3HighLow.text = highLowElaborator(item.list[2].type)
                tide3Time.text = timeFormatter(item.list[2].t)
                tide3Height.text = heightString(item.list[2].v.toDouble())
            }

            if (item.list.size > 3) {
                tide4HighLow.text = highLowElaborator(item.list[3].type)
                tide4Time.text = timeFormatter(item.list[3].t)
                tide4Height.text = heightString(item.list[3].v.toDouble())
            }
            if (item.list.size > 4) {
                tide5HighLow.text = highLowElaborator(item.list[4].type)
                tide5Time.text = timeFormatter(item.list[4].t)
                tide5Height.text = heightString(item.list[4].v.toDouble())
            }
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
            return if (type.contentEquals("L")) {
                "Low"
            } else "High"

        }

        private fun dayFormatter(date: String): String {

            val parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
            return parsedDate.format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy"))
        }
    }


}



