package org.zapomni.venturers.presentation.adapter

import android.view.ViewGroup
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.holder.event.EventProfileHolder

class EventProfileAdapter : BaseAdapter<EventProfileHolder, Event>() {
    override fun getHolder(rootView: ViewGroup, viewType: Int) = EventProfileHolder(rootView)
}