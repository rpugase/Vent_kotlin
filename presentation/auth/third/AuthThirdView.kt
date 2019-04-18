package org.zapomni.venturers.presentation.auth.third

import org.zapomni.venturers.presentation.base.BaseView

interface AuthThirdView : BaseView {
    fun finishAuth()

    fun setNameError(resId: Int)
    fun setSurnameError(resId: Int)

    fun showAvatarDialog()
    fun openGallery()
}