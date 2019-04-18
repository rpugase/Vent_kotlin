package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.dialog_user.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.extensions.toPrettyPhoneNumber
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class UserDialog : BaseDialogFragment() {

    companion object {
        private const val ARG_USER = "ARG_USER"
        private const val ARG_SHOW_PHONE = "ARG_SHOW_PHONE"
        private const val ARG_CAN_SHOW_PHONE = "ARG_CAN_SHOW_PHONE"

        fun newInstance(fragmentManager: FragmentManager?, user: User, canShowPhone: Boolean, showPhone: Boolean): UserDialog {
            return UserDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, user)
                    putBoolean(ARG_CAN_SHOW_PHONE, canShowPhone)
                    putBoolean(ARG_SHOW_PHONE, showPhone)
                }
                show(fragmentManager, BonusDialog::class.java.name)
            }
        }
    }

    var onShowPhoneListener: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_user, null)

        val user = arguments!!.getParcelable<User>(ARG_USER)
        val showPhone = arguments!!.getBoolean(ARG_SHOW_PHONE)
        val canShowPhone = arguments!!.getBoolean(ARG_CAN_SHOW_PHONE)

        with(mView) {
            findViewById<FloatingActionButton>(R.id.btnFinish).setOnClickListener { dismiss() }
            if (canShowPhone) btnShow.setOnClickListener { showPhone(user); onShowPhoneListener?.invoke() }
            else btnShow.visibility = View.GONE

            imgAvatar.loadImage(user?.image)
            tvName.text = "${user?.name} ${user?.surname}"

            val instagram = getString(R.string.instagram_s, user.instagram)
            tvInstagram.text = SpannableString(instagram).apply {
                setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.mainText)),
                        getString(R.string.instagram).length, instagram.length, 0)
            }

            if (showPhone) showPhone(user)
        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }

    override fun setTypeFace() {
        with(mView) {
            tvName.typeface = boldTypeFace
            tvPhone.typeface = boldTypeFace
            tvContactPhone.typeface = regularTypeFace
        }
    }

    private fun showPhone(user: User?) {
        with(mView) {
            btnShow.visibility = View.GONE
            tvAboutPhone.visibility = View.GONE
            tvContactPhone.visibility = View.VISIBLE
            tvPhone.visibility = View.VISIBLE
            tvPhone.text = user?.phoneNumber?.toPrettyPhoneNumber()
        }
    }
}