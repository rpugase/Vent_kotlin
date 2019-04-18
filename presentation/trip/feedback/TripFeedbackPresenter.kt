package org.zapomni.venturers.presentation.trip.feedback

import org.zapomni.venturers.domain.TripInteractor
import org.zapomni.venturers.domain.model.navigation.TripFeedbackNavigation
import org.zapomni.venturers.presentation.base.BasePresenter

class TripFeedbackPresenter(private val tripInteractor: TripInteractor) : BasePresenter<TripFeedbackView>() {

    override fun attachView(view: TripFeedbackView) {
        super.attachView(view)

        val tripFeedbackNavigation = tripInteractor.tripFeedbackNavigation

        tripInteractor.getTrip().execute({
            if (it.isNotEmpty()) when (tripFeedbackNavigation) {
                TripFeedbackNavigation.EVENT -> view.showRatingEvent(it.event)
                TripFeedbackNavigation.CURATOR -> view.showRatingCurator(it.event.curator!!)
                TripFeedbackNavigation.REVIEW -> view.showReview()
                TripFeedbackNavigation.LUCK -> view.showLuck()
                TripFeedbackNavigation.BONUS -> view.showBonus()
                TripFeedbackNavigation.PRIZE -> view.showPrize()
            } else finishFeedback()
        })
    }

    fun finishFeedback() {
        tripInteractor.finishFeedback()
        view?.startMain()
    }
}