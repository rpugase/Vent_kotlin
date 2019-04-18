package org.zapomni.venturers.presentation.trip.feedback.steps

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_trip_feedback_event.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.data.repository.PreferencesRepository
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.domain.model.navigation.TripFeedbackNavigation
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.presentation.base.SimpleFragment
import org.zapomni.venturers.presentation.trip.feedback.TripFeedbackActivity
import java.math.BigDecimal
import java.math.RoundingMode

class TripFeedbackEventFragment : SimpleFragment() {

    companion object {

        private const val ARG_EVENT = "ARG_EVENT"

        fun newInstance(event: Event) = TripFeedbackEventFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_EVENT, event)
            }
        }
    }

    private val preferencesRepository by inject<PreferencesRepository>()

    override fun provideLayoutId() = R.layout.fragment_trip_feedback_event

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rbFeedback.post {
            llNumbers.layoutParams = llNumbers.layoutParams.apply { width = rbFeedback.width }
            (1..10).forEach {
                llNumbers.addView(TextView(context).apply {
                    text = it.toString()
                    setTextColor(Color.WHITE)
                    gravity = Gravity.CENTER
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    layoutParams = ViewGroup.LayoutParams(rbFeedback.width / 10, WRAP_CONTENT)
                })
            }
        }

        rbFeedback.setOnRatingChangeListener { _, rating ->
            if (rating == 0f) {
                btnEstimate.alpha = 0.7f
                btnEstimate.isClickable = false
            } else {
                btnEstimate.alpha = 1f
                btnEstimate.isClickable = true
            }
        }

        showEvent(arguments!!.getParcelable(ARG_EVENT))
    }

    fun showEvent(event: Event) {
        imgEventHead.loadImage(event.headImages.firstOrNull())
        tvEventTitle.text = event.title

        btnEstimate.setOnClickListener {
            preferencesRepository.tripFeedbackNavigation = TripFeedbackNavigation.CURATOR
            preferencesRepository.tripFeedbackCollector = preferencesRepository.tripFeedbackCollector
                    .copy(eventId = event.id, ratingEvent = BigDecimal.valueOf(rbFeedback.rating.toDouble()).setScale(0, RoundingMode.HALF_UP).toInt())

            (activity as TripFeedbackActivity).showRatingCurator(event.curator!!)
        }
        btnEstimate.isClickable = false
    }

    override fun setTypeFace() {
        tvEventTitle.typeface = boldTypeFace
        tvTripFinish.typeface = regularTypeFace
        tvHowTrip.typeface = boldTypeFace
        btnEstimate.typeface = boldTypeFace
    }
}