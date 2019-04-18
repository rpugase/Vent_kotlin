package org.zapomni.venturers.presentation.auth.second

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.UserInteractor
import org.zapomni.venturers.domain.exception.IncorrectSubmitCodeException
import org.zapomni.venturers.domain.model.navigation.ModuleNavigation
import org.zapomni.venturers.presentation.base.BasePresenter
import java.util.concurrent.TimeUnit

class AuthSecondPresenter constructor(private val userInteractor: UserInteractor) : BasePresenter<AuthSecondView>() {

    private val timer = Observable.interval(1, TimeUnit.SECONDS)
    private var timerDisposable = Disposables.disposed()
    var phoneNumber: String? = null
    var canUpdateTimer: Boolean = false

    override fun attachView(view: AuthSecondView) {
        super.attachView(view)
        updateTimer()
    }

    fun sendCodeAgain() {
        if (phoneNumber != null && canUpdateTimer) {
            userInteractor.login(phoneNumber!!)
                    .execute(this::updateTimer)
        }
    }

    fun checkCode(code: String) {
        if (code.length !in 4..6) {
            view?.showErrorForCode(R.string.error_code)
        } else if (phoneNumber != null)
            userInteractor.checkCode(phoneNumber!!, code).execute({
                when (it) {
                    ModuleNavigation.PROFILE -> view?.nextStep()
                    ModuleNavigation.MAIN -> view?.finishAuth()
                }
            }, {
                when (it) {
                    is IncorrectSubmitCodeException -> view?.showErrorForCode(R.string.error_code)
                }
            })
    }

    private fun updateTimer() {
        canUpdateTimer = false
        if (!timerDisposable.isDisposed) {
            timerDisposable.dispose()
        }

        timerDisposable = timer.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it != 59L) {
                        view?.showTimer(59 - it)
                    } else {
                        view?.showTimer(-1)
                        canUpdateTimer = true
                        timerDisposable.dispose()
                    }
                }
    }
}