package org.zapomni.venturers.presentation.auth.third

import org.zapomni.venturers.R
import org.zapomni.venturers.domain.UserInteractor
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.presentation.base.BasePresenter
import java.io.File

class AuthThirdPresenter constructor(private val userInteractor: UserInteractor) : BasePresenter<AuthThirdView>() {

    var imageFile: File? = null

    fun sendProfile(name: String, surname: String) {
        when {
            name.isBlank() -> view?.setNameError(R.string.error_name)
            surname.isBlank() -> view?.setSurnameError(R.string.error_surname)
            imageFile == null -> view?.showAvatarDialog()
            else -> userInteractor.saveUser(User(name = name, surname = surname), imageFile)
                    .execute({ view?.finishAuth() })
        }
    }

    fun clickAvatar() {
        view?.openGallery()
    }
}