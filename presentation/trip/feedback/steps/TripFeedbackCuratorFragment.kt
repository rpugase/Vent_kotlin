package org.zapomni.venturers.presentation.trip.feedback.steps

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_trip_feedback_curator.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.data.repository.PreferencesRepository
import org.zapomni.venturers.domain.model.Curator
import org.zapomni.venturers.domain.model.navigation.TripFeedbackNavigation
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.presentation.base.SimpleFragment
import org.zapomni.venturers.presentation.trip.feedback.TripFeedbackActivity
import java.math.BigDecimal
import java.math.RoundingMode

class TripFeedbackCuratorFragment : SimpleFragment() {

    companion object {
        private val ARG_CURATOR = "ARG_CURATOR"

        fun newInstance(curator: Curator) = TripFeedbackCuratorFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_CURATOR, curator)
            }
        }
    }

    private val preferencesRepository by inject<PreferencesRepository>()

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
                    layoutParams = ViewGroup.LayoutParams(rbFeedback.width / 10, ViewGroup.LayoutParams.WRAP_CONTENT)
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

        setCurator(arguments!!.getParcelable(ARG_CURATOR))
    }

    @SuppressLint("SetTextI18n")
    fun setCurator(curator: Curator) {
        imgAvatar.loadImage(curator.image)
        tvNameCurator.text = "${curator.name} ${curator.surname}"
        tvAboutCurator.text = curator.about

        btnEstimate.setOnClickListener {
            preferencesRepository.tripFeedbackNavigation = TripFeedbackNavigation.REVIEW
            preferencesRepository.tripFeedbackCollector = preferencesRepository.tripFeedbackCollector
                    .copy(ratingCurator = BigDecimal.valueOf(rbFeedback.rating.toDouble()).setScale(0, RoundingMode.HALF_UP).toInt())

            (activity as TripFeedbackActivity).showReview()
        }
        btnEstimate.isClickable = false
    }

    override fun setTypeFace() {
        tvNameCurator.typeface = mediumTypeFace
        tvAboutCurator.typeface = regularTypeFace
        tvHowCurator.typeface = boldTypeFace
        btnEstimate.typeface = boldTypeFace
    }

    override fun provideLayoutId() = R.layout.fragment_trip_feedback_curator
}