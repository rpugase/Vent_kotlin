package org.zapomni.venturers.presentation.holder

import android.view.ViewGroup
import android.widget.TextView
import org.zapomni.venturers.R
import org.zapomni.venturers.presentation.base.BaseViewHolder

class DayMeetHolder(rootView: ViewGroup) : BaseViewHolder<String>(rootView, R.layout.item_meet_day) {
    override fun bind(item: String) {
        (itemView as TextView).apply {
            typeface = regularTypeFace
            text = item
        }
    }
}