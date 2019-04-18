package org.zapomni.venturers.presentation.home

import org.zapomni.venturers.domain.model.*
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.presentation.base.BaseView

interface HomeView : BaseView {

    fun setProgress(max: Int, progress: Int)

    fun loadUser(user: User)

    fun setCard(cardType: CardType)

    fun setChatWithEvent(messages: List<Message>, title: String?, imgHeader: String?, usersCount: Int)

    fun setEvents(events: List<Event>)

    fun runProfile()

    fun showEvent()

    fun showTrips(trips: List<Trip>)

    fun showAdvert(advert: Advert)
}