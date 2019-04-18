package org.zapomni.venturers.presentation.auth.first

import android.util.Patterns
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.UserInteractor
import org.zapomni.venturers.domain.exception.IncorrectPhoneException
import org.zapomni.venturers.presentation.base.BasePresenter

class AuthFirstPresenter constructor(private val userInteractor: UserInteractor) : BasePresenter<AuthFirstView>() {

    fun login(phoneNumber: String) {
        if (Patterns.PHONE.matcher(phoneNumber).matches()) {
            userInteractor.login(phoneNumber)
                    .execute({
                        view?.nextStep()
                    }, {
                        when (it) {
                            is IncorrectPhoneException -> view?.showPhoneNumberError(R.string.error_phone)
                        }
                    })
        } else {
            view?.showPhoneNumberError(R.string.error_phone)
        }
    }
}