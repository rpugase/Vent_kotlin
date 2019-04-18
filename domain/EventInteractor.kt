package org.zapomni.venturers.domain

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.zapomni.venturers.data.repository.EventRepository
import org.zapomni.venturers.domain.mapper.toDomainModel
import org.zapomni.venturers.domain.model.Advert
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.extensions.execute

class EventInteractor(private val eventRepository: EventRepository) {

    private var pageLoading = 0
    private var lastPageLoaded = false
    private val eventsSubject = PublishSubject.create<List<Event>>()

    private val importantEvents = mutableListOf<Event>()

    var openedEvent: Event? = null

    fun getAdvert() = if (false) Single.just(Advert("http://bm.img.com.ua/img/prikol/images/large/0/0/307600.jpg",
            "Хочешь в Буковель?", "У нас осталось два места на Буковель по цене 1800 грн.!", "+380631370489", null)) // "https://www.facebook.com/avanturisty.od/"
    else Single.just(Advert())

    fun getImportantEvents(): Single<List<Event>> {
        return if (importantEvents.isNotEmpty()) Single.just(importantEvents) else eventRepository.getImportantEvents().map {
            return@map if (it.isSuccessful) it.data!!.map { it.toDomainModel()!! }.also { importantEvents.addAll(it) }
            else listOf()
        }
    }

    fun getEvents(): Observable<List<Event>> = eventsSubject

    fun nextEvent() {
        if (!lastPageLoaded) {
            eventRepository.getEvents(pageLoading).execute({
                pageLoading++
                eventsSubject.onNext(it.data!!.map { it.toDomainModel()!! })

                if (it.lastPage) {
                    lastPageLoaded = true
                    eventsSubject.onComplete()
                }
            }, eventsSubject::onError)
        }
    }
}