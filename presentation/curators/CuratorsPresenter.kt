package org.zapomni.venturers.presentation.curators

import org.zapomni.venturers.domain.UserInteractor
import org.zapomni.venturers.presentation.base.BasePresenter

class CuratorsPresenter constructor(private val userInteractor: UserInteractor) : BasePresenter<CuratorsView>() {

    override fun attachView(view: CuratorsView) {
        super.attachView(view)
        userInteractor.getCurators().execute(view::loadConductors)
    }
}