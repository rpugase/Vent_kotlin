package org.zapomni.venturers.presentation.trip

import org.zapomni.venturers.domain.TripInteractor
import org.zapomni.venturers.domain.model.Trip
import org.zapomni.venturers.presentation.base.BasePresenter

class TripPresenter(private val tripInteractor: TripInteractor) : BasePresenter<TripView>() {

    private var trip: Trip? = null

    override fun attachView(view: TripView) {
        super.attachView(view)
        tripInteractor.getTrip().execute({
            if (it.isEmpty()) view.showNoStage()
            else when (it.type) {
                Trip.Type.TRIP_FIRST -> view.showFirstStage(it)
                Trip.Type.TRIP_SECOND -> view.showSecondStage(it)
                Trip.Type.MINI_TRIP -> view.showMiniTrip(it)
            }
        })
    }

    fun openEventsChat() {
        // todo trip must be contains chat-id of events chat
        view?.goToChat()
    }
}