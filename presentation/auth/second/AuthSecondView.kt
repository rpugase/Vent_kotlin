package org.zapomni.venturers.presentation.auth.second

import org.zapomni.venturers.presentation.base.BaseView

interface AuthSecondView : BaseView {
    fun nextStep()
    fun finishAuth()

    fun showTimer(time: Long)
    fun showErrorForCode(resId: Int)
}