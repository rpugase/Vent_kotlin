package org.zapomni.venturers.presentation.holder

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_meet_top.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.presentation.base.BaseViewHolder

class MeetTopHolder(rootView: ViewGroup, private val onBackButtonClickListener: (() -> Unit)? = null) :
        BaseViewHolder<Message>(rootView, R.layout.item_meet_top) {
    override fun bind(item: Message) {
        with(itemView) {
            tvMeets.typeface = boldTypeFace
            btnBack.setOnClickListener { onBackButtonClickListener?.invoke() }
        }
    }
}