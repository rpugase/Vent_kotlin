package org.zapomni.venturers.presentation.splash

import org.zapomni.venturers.presentation.base.BaseView

interface SplashView : BaseView {

    fun showRegistrationView()

    fun showMainView()

    fun showTripView()

    fun stopAnimationRoo()
}