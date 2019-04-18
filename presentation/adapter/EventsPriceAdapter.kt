package org.zapomni.venturers.presentation.adapter

import android.view.ViewGroup
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.holder.event.EventsPriceHolder

class EventsPriceAdapter : BaseAdapter<EventsPriceHolder, Event.InnerDate.Offer>() {
    override fun getHolder(rootView: ViewGroup, viewType: Int) = EventsPriceHolder(rootView)
}