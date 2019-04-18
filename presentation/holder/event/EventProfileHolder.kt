package org.zapomni.venturers.presentation.holder.event

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_event_profile.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.extensions.formatWithSimpleDate
import org.zapomni.venturers.presentation.base.BaseViewHolder

class EventProfileHolder(root: ViewGroup) : BaseViewHolder<Event>(root, R.layout.item_event_profile) {

//    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    override fun bind(item: Event) {
        with(itemView) {
            tvDate.typeface = regularTypeFace
            tvName.typeface = semiBoldTypeFace

            tvDate.text = item.date.startDate.formatWithSimpleDate()
            tvName.text = item.title
        }
    }
}