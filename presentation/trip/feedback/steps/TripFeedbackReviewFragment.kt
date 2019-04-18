package org.zapomni.venturers.presentation.trip.feedback.steps

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_trip_feedback_review.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.data.repository.PreferencesRepository
import org.zapomni.venturers.domain.model.navigation.TripFeedbackNavigation
import org.zapomni.venturers.presentation.base.SimpleFragment
import org.zapomni.venturers.presentation.trip.feedback.TripFeedbackActivity

class TripFeedbackReviewFragment : SimpleFragment() {

    companion object {
        fun newInstance() = TripFeedbackReviewFragment().apply { arguments = Bundle.EMPTY }
    }

    private val preferencesRepository by inject<PreferencesRepository>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnReview.setOnClickListener {
            preferencesRepository.tripFeedbackNavigation = TripFeedbackNavigation.LUCK
            preferencesRepository.tripFeedbackCollector = preferencesRepository.tripFeedbackCollector
                    .copy(review = etReview.text.toString())

            (activity as TripFeedbackActivity).showLuck()
        }

        tvSkip.setOnClickListener {
            preferencesRepository.tripFeedbackNavigation = TripFeedbackNavigation.LUCK
            (activity as TripFeedbackActivity).showLuck()
        }
    }

    override fun setTypeFace() {
        tvReview.typeface = boldTypeFace
        tvReviewAbout.typeface = regularTypeFace
        tvYourReview.typeface = semiBoldTypeFace
        etReview.typeface = regularTypeFace
        btnReview.typeface = boldTypeFace
        tvSkip.typeface = regularTypeFace
    }

    override fun provideLayoutId() = R.layout.fragment_trip_feedback_review
}