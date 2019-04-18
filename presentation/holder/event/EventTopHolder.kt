package org.zapomni.venturers.presentation.holder.event

import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_event_top.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Event
import org.zapomni.venturers.presentation.base.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class EventTopHolder(rootView: ViewGroup, private val onItemClickListener: ((event: Event) -> Unit)? = null) :
        BaseViewHolder<List<Event>>(rootView, R.layout.item_event_top) {
    override fun bind(item: List<Event>) {
        with(itemView) {
            tvImportantEvent.typeface = boldTypeFace

            tvMonthFirst.typeface = mediumTypeFace
            tvDateFirst.typeface = boldTypeFace
            tvAboutFirst.typeface = regularTypeFace
            tvMonthSecond.typeface = mediumTypeFace
            tvDateSecond.typeface = boldTypeFace
            tvAboutSecond.typeface = regularTypeFace
            tvMonthThird.typeface = mediumTypeFace
            tvDateThird.typeface = boldTypeFace
            tvAboutThird.typeface = regularTypeFace

            tvImportantEvent.visibility = View.VISIBLE
            llImportantEvents.visibility = View.VISIBLE

            val monthFormatter = SimpleDateFormat("MMMM", Locale.getDefault())
            val dayFormatter = SimpleDateFormat("dd", Locale.getDefault())

            if (item.size == 3) {
                tvMonthFirst.text = monthFormatter.format(item[0].date.startDate)
                tvDateFirst.text = dayFormatter.format(item[0].date.startDate)
                tvAboutFirst.text = item[0].title

                tvMonthSecond.text = monthFormatter.format(item[1].date.startDate)
                tvDateSecond.text = dayFormatter.format(item[1].date.startDate)
                tvAboutSecond.text = item[1].title

                tvMonthThird.text = monthFormatter.format(item[2].date.startDate)
                tvDateThird.text = dayFormatter.format(item[2].date.startDate)
                tvAboutThird.text = item[2].title

                llFirst.setOnClickListener { onItemClickListener?.invoke(item[0]) }
                llSecond.setOnClickListener { onItemClickListener?.invoke(item[1]) }
                llThird.setOnClickListener { onItemClickListener?.invoke(item[2]) }
            }
        }
    }
}