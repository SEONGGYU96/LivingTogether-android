package com.seoultech.livingtogether_android.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewPagerAdapter<T>() : RecyclerView.Adapter<BaseViewPagerHolder<T, *>>() {

    private var items: MutableList<T> = mutableListOf()

    private var maxItems = items.size

    override fun getItemCount(): Int {
        if (maxItems == 0) {
            return 0
        }
        var count = items.size / maxItems
        if (items.size % maxItems != 0) {
            count++
        }
        return count
    }

    override fun onBindViewHolder(holder: BaseViewPagerHolder<T, *>, position: Int) {
        holder.bind(getSubList(position))
    }

    private fun getSubList(position: Int): List<T> {
        val start = maxItems * position
        val end = start + maxItems

        return if (end > items.size) {
            items.subList(start, items.size)
        } else {
            items.subList(start, end)
        }
    }

    fun setMaxItems(maxItems: Int) {
        this.maxItems = maxItems
    }

    fun setList(data: List<T>) {
        clear()
        addAll(data)
    }


    fun add(data: T) {
        insert(data, items.size)
    }


    fun addAll(data: List<T>?) {
        if (data == null) {
            return
        }

        val startIndex = items.size
        items.addAll(startIndex, data)
        notifyItemRangeInserted(startIndex, data.size)
    }

    fun getItem(position: Int): T {
        return items[position]
    }


    fun insert(data: T, position: Int) {
        items.add(position, data)
        notifyItemInserted(position)
    }


    fun remove(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }


    fun change(position: Int, data: T) {
        items[position] = data
        notifyItemChanged(position)
    }

    fun clear() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }
}