package org.zapomni.venturers.presentation.event.list

import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.presentation.base.BaseView

interface EventListView : BaseView {

    fun setImportantEvent(events: List<Event>)
    fun addEvents(events: List<Event>)
    fun showEvent()
}