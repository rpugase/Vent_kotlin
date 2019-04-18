package org.zapomni.venturers.presentation.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.dialog_reply.view.*
import kotlinx.android.synthetic.main.view_close_button.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.extensions.formatWithSimpleDateChat
import org.zapomni.venturers.extensions.formatWithSimpleDateMeet
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.extensions.toPrettyPhoneNumber
import org.zapomni.venturers.presentation.base.BaseDialogFragment

class ReplyDialog : BaseDialogFragment() {

    var onOpenPhotoListener: ((photoUrl: String) -> Unit)? = null
    var onOpenReplyMessageListener: ((messageId: String) -> Unit)? = null

    companion object {
        private const val ARG_MESSAGE = "ARG_MESSAGE"

        fun newInstance(fragmentManager: FragmentManager?, message: Message): ReplyDialog {
            return ReplyDialog().apply {
                arguments = Bundle().apply { putParcelable(ARG_MESSAGE, message) }
                show(fragmentManager, ReplyDialog::class.java.name)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_reply, null, false)

        val message = arguments?.getParcelable<Message>(ARG_MESSAGE)
        if (message != null) with(mView) {
            btnFinish.setOnClickListener { dialog.dismiss() }
            tvName.text = "${message.user.name} ${message.user.surname}"
            imgAvatar.loadImage(message.user.image)

            if (message.text.isNotBlank()) {
                llMessage.visibility = View.VISIBLE
                tvDate.text = message.time.formatWithSimpleDateChat()
                val spannableMessage = SpannableString("${message.text}00:00")
                spannableMessage.setSpan(ForegroundColorSpan(Color.TRANSPARENT), message.text.length, message.text.length + 5, 0)
                tvMessage.text = spannableMessage
            }

            if (message.photoUrl != null) {
                llPhoto.visibility = View.VISIBLE
                imgPhoto.loadImage(message.photoUrl)
                tvDatePhoto.text = message.time.formatWithSimpleDateChat()
                imgPhoto.setOnClickListener { onOpenPhotoListener?.invoke(message.photoUrl) }
            }

            if (message.meet != null) {
                llMeet.visibility = View.VISIBLE

                tvName.text = message.meet.name
                tvPlace.text = context.getString(R.string.place_colon, message.meet.place)
                tvTime.text = context.getString(R.string.time_colon, message.meet.time.formatWithSimpleDateMeet())
                tvPhone.text = context.getString(R.string.contact_colon, message.user.phoneNumber?.toPrettyPhoneNumber())
                tvDateMeet.text = message.time.formatWithSimpleDateChat()
            }

            if (message.replyMessage != null) {
                rlReply.visibility = View.VISIBLE
                tvNameReply.text = message.replyMessage.user.name
                tvMessageReply.text = message.replyMessage.text
                rlReply.setOnClickListener {
                    dialog.dismiss()
                    onOpenReplyMessageListener?.invoke(message.replyMessage.id)
                }
            }
        }


        return AlertDialog.Builder(context!!)
                .setView(mView)
                .create()
    }
}