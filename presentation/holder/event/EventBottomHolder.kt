package org.zapomni.venturers.presentation.holder.event

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_event_bottom.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.extensions.toPrettyPhoneNumber
import org.zapomni.venturers.presentation.base.BaseViewHolder

class EventBottomHolder(rootView: ViewGroup, private val onCallClickListener: (() -> Unit)? = null) :
        BaseViewHolder<String>(rootView, R.layout.item_event_bottom) {
    override fun bind(item: String) {
        with(itemView) {
            tvPhoneNumber.typeface = boldTypeFace
            tvPhoneNumber.text = item.toPrettyPhoneNumber()
            tvPhoneNumber.setOnClickListener { onCallClickListener?.invoke() }
        }
    }
}