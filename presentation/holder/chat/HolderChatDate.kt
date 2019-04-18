package org.zapomni.venturers.presentation.holder.chat

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_chat_date.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.presentation.base.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class HolderChatDate(rootView: ViewGroup) : BaseViewHolder<Message>(rootView, R.layout.item_chat_date) {
    override fun bind(item: Message) {
        with(itemView) {
            tvDate.text = SimpleDateFormat("dd MMM", Locale.getDefault()).format(item.time)
        }
    }
}