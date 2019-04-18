package org.zapomni.venturers.presentation.trip.feedback.steps

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_trip_feedback_bonus.*
import org.zapomni.venturers.R
import org.zapomni.venturers.presentation.base.SimpleFragment
import org.zapomni.venturers.presentation.trip.feedback.TripFeedbackActivity

class TripFeedbackBonusFragment : SimpleFragment() {

    companion object {
        private val ARG_BONUS = "AGR_BONUS"

        fun newInstance(bonus: Int) = TripFeedbackBonusFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_BONUS, bonus)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvBonus.text = getString(R.string.cash_uah, arguments?.getInt(ARG_BONUS))
        btnNext.setOnClickListener { (activity as TripFeedbackActivity).showPrize() }
    }

    override fun setTypeFace() {
        tvYourBonus.typeface = boldTypeFace
        tvThanksForTrip.typeface = regularTypeFace
        tvBonus.typeface = boldTypeFace
        btnNext.typeface = boldTypeFace
    }

    override fun provideLayoutId() = R.layout.fragment_trip_feedback_bonus
}