package org.zapomni.venturers.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.dialog_message_action.view.*
import kotlinx.android.synthetic.main.view_close_button.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class MessageActionDialog : BaseDialogFragment() {

    companion object {
        const val ARG_SHOW_PHONE = "ARG_SHOW_PHONE"
        const val ARG_ADMIN = "ARG_ADMIN"

        fun newInstance(showPhone: Boolean, admin: Boolean) = MessageActionDialog().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_SHOW_PHONE, showPhone)
                putBoolean(ARG_ADMIN, admin)
            }
        }
    }

    var onReplyListener: (() -> Unit)? = null
    var onShowPhoneListener: (() -> Unit)? = null

    var onDeleteMessageListener: (() -> Unit)? = null
    var onBanDayListener: (() -> Unit)? = null
    var onBanAlwaysListener: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_message_action, null)
        setTypeFace()

        val showPhone = arguments!!.getBoolean(ARG_SHOW_PHONE)
        val admin = arguments!!.getBoolean(ARG_ADMIN)

        with(mView) {
            btnFinish.setOnClickListener { dismiss() }
            if (!showPhone) {
                line1.visibility = View.GONE
                tvShowPhone.visibility = View.GONE
            }
            if (admin) {
                line2.visibility = View.VISIBLE
                line3.visibility = View.VISIBLE
                line4.visibility = View.VISIBLE
                tvDeleteMessage.visibility = View.VISIBLE
                tvBanDay.visibility = View.VISIBLE
                tvBanAlways.visibility = View.VISIBLE

                tvDeleteMessage.setOnClickListener { onDeleteMessageListener?.invoke(); dialog.dismiss() }
                tvBanDay.setOnClickListener { onBanDayListener?.invoke(); dialog.dismiss() }
                tvBanAlways.setOnClickListener { onBanAlwaysListener?.invoke(); dialog.dismiss() }
            }

            tvReply.setOnClickListener { onReplyListener?.invoke(); dialog.dismiss() }
            tvShowPhone.setOnClickListener { onShowPhoneListener?.invoke(); dialog.dismiss() }
        }

        return AlertDialog.Builder(context)
                .setView(mView)
                .create()
    }

    override fun setTypeFace() {
        with(mView) {
            tvReply.typeface = regularTypeFace
            tvShowPhone.typeface = regularTypeFace
        }
    }
}