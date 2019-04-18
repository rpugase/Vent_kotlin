package org.zapomni.venturers.presentation.event.list

import org.zapomni.venturers.domain.EventInteractor
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.presentation.base.BasePresenter

class EventListPresenter(private val eventInteractor: EventInteractor) : BasePresenter<EventListView>() {

    private var isLoadingEvents = false
    private var isLastPageEvents = false

    override fun attachView(view: EventListView) {
        super.attachView(view)
        if (!isLastPageEvents) {
            eventInteractor.getEvents().execute(onNext = {
                view.addEvents(it)
                isLoadingEvents = false
            }, onComplete = { isLastPageEvents = true })
            eventInteractor.nextEvent()
        } else view.addEvents(listOf())

        eventInteractor.getImportantEvents().execute({
            if (it.isNotEmpty()) {
                view.setImportantEvent(it)
            }
        })
    }

    fun loadMoreEvents() {
        if (!isLoadingEvents && !isLastPageEvents) {
            isLoadingEvents = true
            eventInteractor.nextEvent()
        }
    }

    fun openEvent(event: Event) {
        eventInteractor.openedEvent = event
        view?.showEvent()
    }
}