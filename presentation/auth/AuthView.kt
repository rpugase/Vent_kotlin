package org.zapomni.venturers.presentation.auth

import org.zapomni.venturers.presentation.base.BaseView

interface AuthView : BaseView {

    fun loadFirstStep()
    fun loadThirdStep()
}