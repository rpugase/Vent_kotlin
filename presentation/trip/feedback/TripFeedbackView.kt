package org.zapomni.venturers.presentation.trip.feedback

import org.zapomni.venturers.domain.model.Curator
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.presentation.base.BaseView

interface TripFeedbackView : BaseView {

    fun showRatingEvent(event: Event)

    fun showRatingCurator(curator: Curator)

    fun showReview()

    fun showLuck()

    fun showBonus()

    fun showPrize()

    fun finishFeedback()

    fun startMain()
}