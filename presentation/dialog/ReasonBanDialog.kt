package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_reason_ban.view.*
import kotlinx.android.synthetic.main.view_close_button.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class ReasonBanDialog : BaseDialogFragment() {

    companion object {
        private const val ARG_USER = "ARG_USER"

        fun newInstance(user: User) = ReasonBanDialog().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
        }
    }

    var onBanListener: ((reason: String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_reason_ban, null)
        setTypeFace()

        val user = arguments!!.getParcelable<User>(ARG_USER)

        with(mView) {
            btnFinish.setOnClickListener { dismiss() }
            tvName.text = "${user.name} ${user.surname}"

            btnBan.setOnClickListener {
                val textReason = etBanReason.text.toString()
                if (textReason.isBlank()) {
                    etBanReason.requestFocus()
                    etBanReason.error = getString(R.string.error_empty_field)
                } else {
                    onBanListener?.invoke(textReason)
                    dismiss()
                }
            }
        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }

    override fun setTypeFace() {
        with(mView) {
            tvBanDay.typeface = boldTypeFace
            tvName.typeface = regularTypeFace
            tvBanReason.typeface = semiBoldTypeFace
            btnBan.typeface = boldTypeFace
        }
    }
}