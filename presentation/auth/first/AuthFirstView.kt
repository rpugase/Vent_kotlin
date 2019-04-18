package org.zapomni.venturers.presentation.auth.first

import org.zapomni.venturers.presentation.base.BaseView

interface AuthFirstView : BaseView {
    fun nextStep()
    fun showPhoneNumberError(resId: Int)
}