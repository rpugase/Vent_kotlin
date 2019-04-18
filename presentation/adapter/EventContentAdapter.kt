package org.zapomni.venturers.presentation.adapter

import android.view.ViewGroup
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.holder.EventContentHolder

class EventContentAdapter : BaseAdapter<EventContentHolder, EventContentHolder.EventContent>() {

    private var lastSelect = 0
    var onContentClickListener: ((position: Int) -> Unit)? = null

    private val onContentClickListenerInner: (position: Int) -> Unit = {
        selectItem(it)
        onContentClickListener?.invoke(it)
    }

    override fun getHolder(rootView: ViewGroup, viewType: Int) = EventContentHolder(rootView, onContentClickListenerInner)

    override fun setItems(items: List<EventContentHolder.EventContent>?) {
        super.setItems(items)
        selectItem(0)
    }

    private fun selectItem(position: Int) {
        items[lastSelect].isSelected = false
        items[position].isSelected = true
        notifyItemChanged(lastSelect)
        notifyItemChanged(position)
        lastSelect = position
    }
}