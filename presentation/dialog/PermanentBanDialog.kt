package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_permanent_ban.view.*
import kotlinx.android.synthetic.main.view_close_button.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class PermanentBanDialog : BaseDialogFragment() {

    companion object {
        private const val ARG_USER = "ARG_USER"

        fun newInstance(user: User) = PermanentBanDialog().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
        }
    }

    var onBanListener: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_permanent_ban, null)
        setTypeFace()

        val user = arguments!!.getParcelable<User>(ARG_USER)

        with(mView) {
            btnFinish.setOnClickListener { dismiss() }
            btnCancel.setOnClickListener { dismiss() }
            btnBan.setOnClickListener { onBanListener?.invoke(); dismiss() }
            tvTitle.text = getString(R.string.you_want_ban_always, user.name, user.surname)
        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }

    override fun setTypeFace() {
        with(mView) {
            tvTitle.typeface = boldTypeFace
            tvMessage.typeface = semiBoldTypeFace
            btnBan.typeface = boldTypeFace
        }
    }
}