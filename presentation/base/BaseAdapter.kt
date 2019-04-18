package org.zapomni.venturers.presentation.base

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class BaseAdapter<H : BaseViewHolder<T>, T> : RecyclerView.Adapter<H>() {

    val items = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        return getHolder(parent, viewType)
    }

    abstract fun getHolder(rootView: ViewGroup, viewType: Int): H

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: H, position: Int) {
        holder.bind(items[position])
    }

    open fun setItems(items: List<T>?) {
        if (items != null) {
            this.items.clear()
            this.items.addAll(items)
            notifyDataSetChanged()
        } else {
            this.items.clear()
            notifyDataSetChanged()
        }
    }

    open fun addItemsToTop(items: List<T>?) {
        if (items != null) {
            this.items.addAll(0, items)
            notifyItemRangeInserted(0, items.size)
        }
    }

    open fun addItems(items: List<T>?) {
        if (items != null) {
            val oldSize = this.items.size
            this.items.addAll(items)
            notifyItemRangeInserted(oldSize, items.size)
        }
    }

    open fun addItem(item: T?) {
        if (item != null) {
            items.add(item)
            notifyItemInserted(items.lastIndex)
        }
    }
}