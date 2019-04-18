package org.zapomni.venturers.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.presentation.holder.event.EventBottomHolder
import org.zapomni.venturers.presentation.holder.event.EventHolder
import org.zapomni.venturers.presentation.holder.event.EventTopHolder
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VT_TOP = 0
    private val VT_EVENT = 1
    private val VT_BOTTOM = 2

    private var lastMonth = ""

    var onItemClickListener: ((event: Event) -> Unit)? = null
    var onPhoneClickListener: ((event: String) -> Unit)? = null

    private val importantEvents = mutableListOf<Event>()
    private val events = mutableListOf<Event>()
    private val phoneNumber = "380632125566"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        VT_TOP -> EventTopHolder(parent) { onItemClickListener?.invoke(it) }
        VT_EVENT -> EventHolder(parent) { onItemClickListener?.invoke(it) }
        VT_BOTTOM -> EventBottomHolder(parent) { onPhoneClickListener?.invoke(phoneNumber) }
        else -> throw IllegalArgumentException("No such viewType")
    }


    override fun getItemCount() =
            if (events.isNotEmpty() && importantEvents.isNotEmpty()) events.size + 2
            else if (events.isNotEmpty() && importantEvents.isEmpty()) events.size + 1
            else 0

    override fun getItemViewType(position: Int) = when {
        importantEvents.isNotEmpty() -> when {
            position == 0 -> VT_TOP
            position > 0 && position <= events.size -> VT_EVENT
            position == events.size + 1 -> VT_BOTTOM
            else -> VT_EVENT
        }
        else -> when {
            position <= events.size - 1 -> VT_EVENT
            position == events.size -> VT_BOTTOM
            else -> VT_EVENT
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            importantEvents.isNotEmpty() -> when (position) {
                0 -> (holder as EventTopHolder).bind(importantEvents)
                events.size + 1 -> (holder as EventBottomHolder).bind(phoneNumber)
                else -> (holder as EventHolder).bind(events[position - 1])
            }
            else -> when (position) {
                events.size -> (holder as EventBottomHolder).bind(phoneNumber)
                else -> (holder as EventHolder).bind(events[position])
            }
        }
    }

    fun setImportantEvents(events: List<Event>) {
        importantEvents.clear()
        importantEvents.addAll(events)
        notifyItemChanged(0)
    }

    fun addItems(items: List<Event>) {
        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        items.forEach {
            val month = dateFormat.format(it.date.startDate).toUpperCase()
            it.needShowDate = month != lastMonth
            if (it.needShowDate) {
                lastMonth = month
            }
        }

        val lastPosition = events.size
        events.addAll(items)
        notifyItemRangeInserted(lastPosition, items.size)
    }
}