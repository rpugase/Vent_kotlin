package org.zapomni.venturers.presentation.profile.edit

import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.presentation.base.BaseView

interface ProfileEditView : BaseView {
    fun setUser(user: User)
    fun saveChanged()

    fun setNameError(resId: Int)
    fun setSurnameError(resId: Int)

    fun openGallery()
}