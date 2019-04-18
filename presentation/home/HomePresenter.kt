package org.zapomni.venturers.presentation.home

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import org.zapomni.venturers.domain.ChatInteractor
import org.zapomni.venturers.domain.EventInteractor
import org.zapomni.venturers.domain.TripInteractor
import org.zapomni.venturers.domain.UserInteractor
import org.zapomni.venturers.domain.model.Advert
import org.zapomni.venturers.domain.model.CardType
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.domain.model.Trip
import org.zapomni.venturers.domain.model.navigation.TripNavigation
import org.zapomni.venturers.presentation.base.BasePresenter

class HomePresenter(private val userInteractor: UserInteractor,
                    private val eventInteractor: EventInteractor,
                    private val chatInteractor: ChatInteractor,
                    private val tripInteractor: TripInteractor) : BasePresenter<HomeView>() {

    override fun attachView(view: HomeView) {
        super.attachView(view)
        Single.zip(eventInteractor.getImportantEvents(), tripInteractor.getTrips(), eventInteractor.getAdvert(),
                Function3<List<Event>, List<Trip>, Advert, Home> { events, trips, advert -> Home(events, trips, advert) })
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    val tripNavigation = tripInteractor.tripNavigation

                    when (tripNavigation) {
                        TripNavigation.NO_TRIP, TripNavigation.MINI_TRIP -> {
                            if (!it.advert.isEmpty()) view.showAdvert(it.advert)
                            else view.setEvents(it.events)
                        }
                        else -> view.showTrips(it.trips)
                    }

                    if (tripNavigation == TripNavigation.MINI_TRIP) {
                        view.showTrips(it.trips)
                    }

                    if (tripInteractor.tripNavigation in listOf(TripNavigation.NO_TRIP, TripNavigation.MINI_TRIP) || it.trips.firstOrNull()?.event?.chatId == null)
                        ChatInteractor.MAIN_CHAT else it.trips.first().event.chatId!!
                }
                .observeOn(Schedulers.io())
                .flatMap(chatInteractor::loadChat)
                .execute({
                    val title = if (it.agenda != null) it.agenda?.theme else if (it.poll != null) it.poll?.question else null
                    val imgHead = if (it.agenda != null) it.agenda?.photoId else if (it.poll != null) it.poll?.photoId else null
                    view.setChatWithEvent(it.messages
                            .takeLast(if (title != null) 1 else 3)
                            .map {
                                it.copy(text = when {
                                    it.photoUrl != null -> "Фото"
                                    it.chest != null -> "Сундук"
                                    it.meet != null -> "Встреча"
                                    else -> it.text
                                })
                            }, title, imgHead, it.usersCount)
        })

//        eventInteractor.getImportantEvents().execute(view::setEvents)
//
//        chatInteractor.loadChat().execute({
//            val title = if (it.agenda != null) it.agenda?.theme else if (it.poll != null) it.poll?.question else null
//            val imgHead = if (it.agenda != null) it.agenda?.photoId else if (it.poll != null) it.poll?.photoId else null
//            view.setChatWithEvent(it.messages.filter { it.text.isNotBlank() }.takeLast(if (title != null) 1 else 3), title, imgHead, it.usersCount)
//        })

        userInteractor.getUser()
                .execute({
                    view.loadUser(it)
                    view.setCard(it.bonusCard.cardType)
                    when (it.bonusCard.cardType) {
                        CardType.NONE -> view.setProgress(0, 0)
                        CardType.GOLD -> view.setProgress(5, it.events.size)
                        CardType.GREEN -> view.setProgress(25, it.events.size)
                        CardType.BLUE -> view.setProgress(50, it.events.size)
                    }
                })
    }

    fun showEvent(event: Event) {
        eventInteractor.openedEvent = event
        view?.showEvent()
    }

    private inner class Home(val events: List<Event>, val trips: List<Trip>, val advert: Advert)
}