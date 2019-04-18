package org.zapomni.venturers.presentation.auth

import org.zapomni.venturers.domain.UserInteractor
import org.zapomni.venturers.domain.model.navigation.AuthNavigation
import org.zapomni.venturers.presentation.base.BasePresenter

class AuthPresenter constructor(private val userInteractor: UserInteractor) : BasePresenter<AuthView>() {

    override fun attachView(view: AuthView) {
        super.attachView(view)
        when (userInteractor.authNavigation) {
            AuthNavigation.PHONE -> view.loadFirstStep()
            AuthNavigation.PROFILE -> view.loadThirdStep()
        }
    }
}