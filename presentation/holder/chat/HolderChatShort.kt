package org.zapomni.venturers.presentation.holder.chat

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_chat_short.view.*
import org.ocpsoft.prettytime.PrettyTime
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.presentation.base.BaseViewHolder
import java.util.*

class HolderChatShort(rootView: ViewGroup) : BaseViewHolder<Message>(rootView, R.layout.item_chat_short) {
    override fun bind(item: Message) {
        with(itemView) {
            imgAvatar.loadImage(item.user.image)
            tvName.text = item.user.name
            tvMessage.text = item.text
            tvDate.text = PrettyTime(Locale.getDefault()).format(item.time)
        }
    }
}