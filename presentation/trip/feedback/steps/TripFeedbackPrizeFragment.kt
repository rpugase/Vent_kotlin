package org.zapomni.venturers.presentation.trip.feedback.steps

import android.os.Bundle
import android.support.annotation.IntRange
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.fragment_trip_feedback_prize.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.data.repository.PreferencesRepository
import org.zapomni.venturers.presentation.base.SimpleFragment
import org.zapomni.venturers.presentation.trip.feedback.TripFeedbackActivity

class TripFeedbackPrizeFragment : SimpleFragment() {

    companion object {
        private const val ARG_PRIZE = "ARG_PRIZE"

        fun newInstance(prize: Int) = TripFeedbackPrizeFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PRIZE, prize)
            }
        }
    }

    private var prize = 0
    private val preferencesRepository by inject<PreferencesRepository>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prize = arguments!!.getInt(ARG_PRIZE)

        tvTakeChest.text = getString(R.string.take_chest_and_more, prize)

        btnFirstChest.setOnClickListener { pickChest(1) }
        btnSecondChest.setOnClickListener { pickChest(2) }
        btnThirdChest.setOnClickListener { pickChest(3) }

        btnNext.setOnClickListener { (activity as TripFeedbackActivity).finishFeedback() }
        btnNext.isClickable = false

        val prizeId = preferencesRepository.tripFeedbackCollector.prizeId

        if (prizeId.isNotEmpty()) {
            pickChest(prizeId.toInt())
        }
    }

    private fun pickChest(@IntRange(from = 1, to = 3) chest: Int) {
        val view = when (chest) {
            1 -> btnFirstChest
            2 -> btnSecondChest
            3 -> btnThirdChest
            else -> btnFirstChest
        }

        preferencesRepository.tripFeedbackCollector = preferencesRepository.tripFeedbackCollector
                .copy(prizeId = chest.toString()) // todo copy real id

        btnNext.text = getString(R.string.next)
        btnNext.isClickable = true
        btnNext.alpha = 1f
        btnFirstChest.isClickable = false
        btnSecondChest.isClickable = false
        btnThirdChest.isClickable = false

        view.setImageResource(R.drawable.chest_open_gold)

//        val randomWinner = Random().nextInt() % 3 + 1

        tvPrize.visibility = View.VISIBLE
        tvPrize.text = getString(R.string.cash_uah, prize)
        view.post {
            tvPrize.layoutParams = (tvPrize.layoutParams as FrameLayout.LayoutParams).apply {
                val marginStart = if (chest == 3) view.x else view.x + view.width / 2
                setMargins(marginStart.toInt(), view.y.toInt() / 2, 0, 0)
            }
        }
    }

    override fun setTypeFace() {
        tvYourPrize.typeface = boldTypeFace
        tvTakeChest.typeface = regularTypeFace
        tvPrize.typeface = boldTypeFace
        btnNext.typeface = boldTypeFace
    }

    override fun provideLayoutId() = R.layout.fragment_trip_feedback_prize
}