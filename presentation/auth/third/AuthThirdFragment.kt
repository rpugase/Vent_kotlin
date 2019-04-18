package org.zapomni.venturers.presentation.auth.third

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_auth_third.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.extensions.getRealPathFromUri
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.extensions.pickImageFromGallery
import org.zapomni.venturers.presentation.base.BaseFragment
import org.zapomni.venturers.presentation.dialog.LoadAvatarDialog
import org.zapomni.venturers.presentation.main.MainActivity
import java.io.File

class AuthThirdFragment : BaseFragment<AuthThirdView, AuthThirdPresenter>(), AuthThirdView {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgAvatar.setOnClickListener { presenter.clickAvatar() }
        btnNext.setOnClickListener { presenter.sendProfile(etName.text.toString(), etSurname.text.toString()) }
    }

    override fun setTypeFace() {
        tvRegistration.typeface = boldTypeFace
        tvNameImportant.typeface = mediumTypeFace
        tvName.typeface = semiBoldTypeFace
        etName.typeface = regularTypeFace
        tvSurname.typeface = semiBoldTypeFace
        etSurname.typeface = regularTypeFace
        btnNext.typeface = boldTypeFace
    }

    override fun finishAuth() {
        MainActivity.start(context!!)
        activity?.finish()
    }

    override fun setNameError(resId: Int) {
        etName.error = getString(resId)
        etName.requestFocus()
    }

    override fun setSurnameError(resId: Int) {
        etSurname.error = getString(resId)
        etName.requestFocus()
    }

    override fun showAvatarDialog() {
        hideKeyBoard()
        val dialog = LoadAvatarDialog()
        dialog.show(fragmentManager, LoadAvatarDialog::class.java.name)
        dialog.onUnderstandingClickListener = { openGallery() }
    }

    override fun openGallery() {
        activity?.pickImageFromGallery {
            imgAvatar.loadImage(it)
            presenter.imageFile = File(context?.getRealPathFromUri(it))
        }
    }

    override fun provideLayoutId() = R.layout.fragment_auth_third

    override fun createPresenter() = inject<AuthThirdPresenter>().value
}