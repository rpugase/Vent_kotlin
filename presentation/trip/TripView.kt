package org.zapomni.venturers.presentation.trip

import org.zapomni.venturers.domain.model.Trip
import org.zapomni.venturers.presentation.base.BaseView

interface TripView : BaseView {

    fun goToChat()

    fun showNoStage()
    fun showFirstStage(trip: Trip)
    fun showSecondStage(trip: Trip)
    fun showMiniTrip(trip: Trip)
}