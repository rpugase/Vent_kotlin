package org.zapomni.venturers.presentation.holder.event

import android.annotation.SuppressLint
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_price_event.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.extensions.getDisplayNameShort
import org.zapomni.venturers.presentation.base.BaseViewHolder

class EventsPriceHolder(rootView: ViewGroup) : BaseViewHolder<Event.InnerDate.Offer>(rootView, R.layout.item_price_event) {
    @SuppressLint("SetTextI18n")
    override fun bind(item: Event.InnerDate.Offer) {
        with(itemView) {
            tvAbout.typeface = regularTypeFace
            tvPrice.typeface = boldTypeFace

            tvAbout.text = item.name
            tvPrice.text = "${item.offerTypes[0].price} ${context.getString(item.currency.getDisplayNameShort())}"
        }
    }
}