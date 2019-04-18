package org.zapomni.venturers.presentation.splash

import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.presentation.auth.AuthActivity
import org.zapomni.venturers.presentation.base.BaseActivity
import org.zapomni.venturers.presentation.main.MainActivity
import org.zapomni.venturers.presentation.trip.feedback.TripFeedbackActivity

class SplashActivity : BaseActivity<SplashView, SplashPresenter>(), SplashView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        lavRoo.addAnimatorUpdateListener {
            if (it.animatedValue.toString() == "1.0") {
                presenter.goNext()
            }
        }
    }

    override fun stopAnimationRoo() {
        lavRoo.pauseAnimation()
        lavRoo.removeAllUpdateListeners()
    }

    override fun createPresenter() = inject<SplashPresenter>().value

    override fun showRegistrationView() {
        AuthActivity.start(this)
        finish()
    }

    override fun showMainView() {
        MainActivity.start(this)
        finish()
    }

    override fun showTripView() {
        startActivity<TripFeedbackActivity>()
        finish()
    }
}