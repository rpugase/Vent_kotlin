package org.zapomni.venturers.presentation.event

import io.reactivex.Observable
import org.zapomni.venturers.domain.EventInteractor
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.presentation.base.BasePresenter
import java.util.*
import java.util.concurrent.TimeUnit

class EventPresenter(private val eventInteractor: EventInteractor) : BasePresenter<EventView>() {

    private lateinit var event: Event
    private var headImageIndex = 0

    override fun attachView(view: EventView) {
        super.attachView(view)
        event = eventInteractor.openedEvent!!
        view.prefetchImages(event.headImages.toMutableList().apply { addAll(event.photos) })
        view.setEvent(event)

        setDays()

        view.showHeadImage(event.headImages[headImageIndex], event.headImages.size > 1)

        if (event.headImages.size > 1) {
            Observable.interval(3, TimeUnit.SECONDS)
                    .execute({
                        headImageIndex = if (headImageIndex < event.headImages.size - 1) headImageIndex + 1 else 0
                        view.showHeadImage(event.headImages[headImageIndex], true)
                    }, withLoading = false)
        }
    }

    fun showPhoto(position: Int) {
        view?.showPhotos(event.photos, position)
    }

    private fun setDays() {
        val calendar = Calendar.getInstance()

        calendar.time = event.date.endDate
        var endDay = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.time = event.date.startDate
        var startDay = calendar.get(Calendar.DAY_OF_MONTH)

        if (startDay < endDay) {
            startDay++
            endDay--
            view?.showDays((startDay..endDay).toList().map(Int::toString))
        } else if (startDay > endDay) {
            val dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            val days = mutableListOf<String>()

            startDay++
            endDay--

            (0..dayOfMonth - startDay).forEach { days.add((startDay + it).toString()) }
            (endDay - 1 downTo 0).forEach { days.add((endDay - it).toString()) }
            view?.showDays(days)
        }
    }
}