package org.zapomni.venturers.presentation.trip.feedback.steps

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_trip_feedback_luck.*
import kotlinx.android.synthetic.main.view_progress.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.data.repository.PreferencesRepository
import org.zapomni.venturers.domain.model.CardType
import org.zapomni.venturers.domain.model.navigation.TripFeedbackNavigation
import org.zapomni.venturers.presentation.base.SimpleFragment
import org.zapomni.venturers.presentation.trip.feedback.TripFeedbackActivity

class TripFeedbackLuckFragment : SimpleFragment() {

    companion object {
        fun newInstance() = TripFeedbackLuckFragment().apply { arguments = Bundle.EMPTY }
    }

    private val preferencesRepository by inject<PreferencesRepository>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lavMan.addAnimatorUpdateListener {
            if (it.animatedValue.toString() == "1.0") {
                showResult()
            }
        }

        btnNext.setOnClickListener {
            preferencesRepository.tripFeedbackNavigation = TripFeedbackNavigation.BONUS
            (activity as TripFeedbackActivity).showBonus()
        }
    }

    fun showResult() {
        lavMan.animate()
                .alpha(0f)
                .setDuration(1000)
                .withEndAction { lavMan.visibility = View.GONE }
                .start()
        llResult.animate()
                .alpha(1f)
                .setDuration(1000)
                .withStartAction { llResult.visibility = View.VISIBLE }
                .start()

        tvSuccessAbout.text = getString(R.string.count_of_trip_before_card)

        val user = preferencesRepository.user!!
        user.addEvent()

        when (user.bonusCard.cardType) {
            CardType.NONE -> setProgress(0, 0)
            CardType.GOLD -> setProgress(5, user.events.size)
            CardType.GREEN -> setProgress(25, user.events.size)
            CardType.BLUE -> setProgress(50, user.events.size)
        }

        when (user.bonusCard.cardType) {
            CardType.GOLD -> imgCard.setImageResource(R.drawable.ic_card_gold)
            CardType.GREEN -> imgCard.setImageResource(R.drawable.ic_card_green)
            CardType.BLUE -> imgCard.setImageResource(R.drawable.ic_card_blue)
            CardType.NONE -> {
                imgCardNo.visibility = View.VISIBLE
                imgCard.visibility = View.GONE
            }
        }
    }

    private fun setProgress(max: Int, progress: Int) {
        this.progress.also {
            it.max = max
            it.progress = progress
        }
        tvProgress.text = getString(R.string.progress_text, progress, max)
    }

    override fun setTypeFace() {
        tvSuccess.typeface = boldTypeFace
        tvSuccessAbout.typeface = regularTypeFace
        tvProgress.typeface = mediumTypeFace
        tvGold.typeface = semiBoldTypeFace
        tvGreen.typeface = semiBoldTypeFace
        tvBlue.typeface = semiBoldTypeFace
        btnNext.typeface = boldTypeFace
        tvHowWork.typeface = boldTypeFace
    }

    override fun provideLayoutId() = R.layout.fragment_trip_feedback_luck
}