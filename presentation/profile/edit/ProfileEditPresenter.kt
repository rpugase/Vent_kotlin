package org.zapomni.venturers.presentation.profile.edit

import org.zapomni.venturers.R
import org.zapomni.venturers.domain.UserInteractor
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.presentation.base.BasePresenter
import java.io.File

class ProfileEditPresenter constructor(private val userInteractor: UserInteractor) : BasePresenter<ProfileEditView>() {

    private var user: User? = null
    var imageFile: File? = null

    override fun attachView(view: ProfileEditView) {
        super.attachView(view)
        if (user == null) {
            userInteractor.getUser().execute(view::setUser)
        } else view.setUser(user!!)
    }

    fun saveProfile(name: String, surname: String, instagram: String) {
        when {
            name.isBlank() -> view?.setNameError(R.string.error_name)
            surname.isBlank() -> view?.setSurnameError(R.string.error_surname)
            else -> userInteractor.saveUser(User(name = name, surname = surname, instagram = instagram), imageFile)
                    .execute({ view?.saveChanged() })
        }
    }
}