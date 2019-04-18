package org.zapomni.venturers.presentation.holder.chat

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_chat_me.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.extensions.formatWithSimpleDateChat
import org.zapomni.venturers.extensions.formatWithSimpleDateMeet
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.extensions.toPrettyPhoneNumber
import org.zapomni.venturers.presentation.base.BaseViewHolder
import org.zapomni.venturers.presentation.chat.ChatPresenter

class HolderChatMe(rootView: ViewGroup, private val presenter: ChatPresenter) : BaseViewHolder<Message>(rootView, R.layout.item_chat_me) {
    override fun bind(item: Message) {
        with(itemView) {
            setIsRecyclable(defaultRecyclable)
            tvMessage.typeface = regularTypeFace
            tvDate.typeface = italicTypeFace
            tvDatePhoto.typeface = italicTypeFace
            tvDateMeet.typeface = italicTypeFace
            tvMeetLabel.typeface = semiBoldTypeFace
            tvName.typeface = boldTypeFace
            tvPlace.typeface = regularTypeFace
            tvTime.typeface = regularTypeFace
            tvPhone.typeface = regularTypeFace

            llMessage.visibility = View.GONE
            llMeet.visibility = View.GONE
            llPhoto.visibility = View.GONE
            rlReply.visibility = View.GONE

            tvOnTopic.visibility = if (item.onTopic && item.meet == null) View.VISIBLE else View.GONE
            tvOnTopicMeet.visibility = if (item.onTopic && item.meet != null) View.VISIBLE else View.GONE
            tvDate.visibility = if (item.meet != null || item.photoUrl != null) View.GONE else View.VISIBLE
            imgAvatar.visibility = if (item.showAvatar) {
                imgAvatar.loadImage(item.user.image); View.VISIBLE
            } else View.INVISIBLE

            if (item.text.isNotBlank()) {
                llMessage.visibility = View.VISIBLE
                tvDate.text = item.time.formatWithSimpleDateChat()
                setOnLongClickListener { presenter.onMessageAction(item);true }
                val spannableMessage = SpannableString("${item.text}00:00")
                spannableMessage.setSpan(ForegroundColorSpan(Color.TRANSPARENT), item.text.length, item.text.length + 5, 0)
                tvMessage.text = spannableMessage
            }

            if (item.photoUrl != null) {
                llPhoto.visibility = View.VISIBLE
                imgPhoto.loadImage(item.photoUrl)
                tvDatePhoto.text = item.time.formatWithSimpleDateChat()
                imgPhoto.setOnClickListener { presenter.openPhoto(item.photoUrl) }
            }

            if (item.meet != null) {
                llMeet.visibility = View.VISIBLE

                tvName.text = item.meet.name
                tvPlace.text = context.getString(R.string.place_colon, item.meet.place)
                tvTime.text = context.getString(R.string.time_colon, item.meet.time.formatWithSimpleDateMeet())
                tvPhone.text = context.getString(R.string.contact_colon, item.user.phoneNumber?.toPrettyPhoneNumber())
                tvDateMeet.text = item.time.formatWithSimpleDateChat()
            }

            cbLike.checked = item.likePressed
            cbLike.countLikes = item.likes
            cbLike.onCheckedChangeListener = { presenter.doOnLike(item.id, it) }

            if (item.replyMessage != null) {
                rlReply.visibility = View.VISIBLE
                tvNameReply.text = item.replyMessage.user.name
                tvMessageReply.text = item.replyMessage.text
                rlReply.setOnClickListener { presenter.showReplyMessage(item.replyMessage.id) }
            }
        }
    }
}