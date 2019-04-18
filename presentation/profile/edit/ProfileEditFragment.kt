package org.zapomni.venturers.presentation.profile.edit

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import org.koin.android.ext.android.inject
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.extensions.getRealPathFromUri
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.extensions.pickImageFromGallery
import org.zapomni.venturers.extensions.toPrettyPhoneNumber
import org.zapomni.venturers.presentation.base.BaseFragment
import java.io.File

class ProfileEditFragment : BaseFragment<ProfileEditView, ProfileEditPresenter>(), ProfileEditView {

    companion object {
        fun newInstance() = ProfileEditFragment().apply {
            arguments = Bundle()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imgAvatar.setOnClickListener { openGallery() }
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnSave.setOnClickListener { presenter.saveProfile(
                etName.text.toString(),
                etSurname.text.toString(),
                etInstagram.text.toString()
        ) }
    }

    override fun setUser(user: User) {
        tvName.text = "${user.name} ${user.surname}"
        tvPhone.text = user.phoneNumber?.toPrettyPhoneNumber()

        imgAvatar.loadImage(user.image)
        etName.setText(user.name)
        etSurname.setText(user.surname)
        etInstagram.setText(user.instagram)
    }

    override fun saveChanged() {
        activity?.onBackPressed()
    }

    override fun setNameError(resId: Int) {
        etName.error = getString(resId)
    }

    override fun setSurnameError(resId: Int) {
        etSurname.error = getString(resId)
    }

    override fun openGallery() {
        activity?.pickImageFromGallery {
            imgAvatar.loadImage(it)
            presenter.imageFile = File(context?.getRealPathFromUri(it))
        }
    }

    override fun provideLayoutId() = R.layout.fragment_profile_edit

    override fun createPresenter() = inject<ProfileEditPresenter>().value

    override fun setTypeFace() {
        tvName.typeface = boldTypeFace
        tvPhone.typeface = regularTypeFace
        tvNameTitle.typeface = semiBoldTypeFace
        etName.typeface = regularTypeFace
        tvSurname.typeface = semiBoldTypeFace
        etSurname.typeface = regularTypeFace
        btnSave.typeface = boldTypeFace
    }
}