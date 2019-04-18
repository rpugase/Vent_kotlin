package org.zapomni.venturers.presentation.holder

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.item_trip.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Trip
import org.zapomni.venturers.extensions.dpToPx
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.presentation.base.BaseViewHolder

class TripHolder(rootView: ViewGroup) : BaseViewHolder<Trip>(rootView, R.layout.item_trip) {
    override fun bind(item: Trip) {
        with(itemView) {
            tvEventTitle.typeface = boldTypeFace
            tvYouAreOnWay.typeface = boldTypeFace
            tvBeforeStart.typeface = boldTypeFace

            imgEventHead.loadImage(item.event.headImages.firstOrNull())
            tvEventTitle.text = item.event.title

            var topBottomMargin = 18.dpToPx()

            if (item.event.date.startDate.time < System.currentTimeMillis()) {
                flipClock.visibility = View.GONE
                tvBeforeStart.visibility = View.GONE
                tvYouAreOnWay.visibility = View.VISIBLE
                topBottomMargin = 36.dpToPx()
            } else flipClock.start(item.event.date.startDate.time)

            llTripInner.layoutParams = (llTripInner.layoutParams as FrameLayout.LayoutParams).apply {
                setMargins(18.dpToPx(), topBottomMargin, 18.dpToPx(), topBottomMargin)
            }
        }
    }
}