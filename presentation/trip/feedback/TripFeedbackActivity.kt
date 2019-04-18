package org.zapomni.venturers.presentation.trip.feedback

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Curator
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.presentation.base.BaseActivity
import org.zapomni.venturers.presentation.main.MainActivity
import org.zapomni.venturers.presentation.trip.feedback.steps.*

class TripFeedbackActivity : BaseActivity<TripFeedbackView, TripFeedbackPresenter>(), TripFeedbackView {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
    }

    override fun showRatingEvent(event: Event) {
        TripFeedbackEventFragment.newInstance(event).replace()
    }

    override fun showRatingCurator(curator: Curator) {
        TripFeedbackCuratorFragment.newInstance(curator).replace()
    }

    override fun showReview() {
        TripFeedbackReviewFragment.newInstance().replace()
    }

    override fun showLuck() {
        TripFeedbackLuckFragment.newInstance().replace()
    }

    override fun showBonus() {
        TripFeedbackBonusFragment.newInstance(223).replace()
    }

    override fun showPrize() {
        TripFeedbackPrizeFragment.newInstance(200).replace()
    }

    override fun finishFeedback() {
        presenter.finishFeedback()
    }

    override fun startMain() {
        MainActivity.start(this)
        finish()
    }

    override fun createPresenter() = inject<TripFeedbackPresenter>().value

    private fun Fragment.replace() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, this, javaClass.simpleName)
                .commit()
    }
}