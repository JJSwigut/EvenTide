package com.jjswigut.eventide.utils

import androidx.recyclerview.widget.DiffUtil
import com.jjswigut.eventide.data.entities.TidalStation


class ListDiffCallback(
    private val oldList: List<TidalStation>,
    private val newList: List<TidalStation>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition].name == newList[newPosition].name
    }

}