package com.jjswigut.eventide.ui.tides

import androidx.recyclerview.widget.DiffUtil
import com.jjswigut.eventide.data.entities.UIModel


class TidesDiffCallback(
    private val oldList: List<UIModel>,
    private val newList: List<UIModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition] == newList[newPosition]
    }

}