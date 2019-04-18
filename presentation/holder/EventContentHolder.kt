package org.zapomni.venturers.presentation.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_event_content.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.extensions.dpToPx
import org.zapomni.venturers.presentation.base.BaseViewHolder

class EventContentHolder(rootView: ViewGroup, private val onContentClickListener: ((position: Int) -> Unit)? = null) :
        BaseViewHolder<EventContentHolder.EventContent>(rootView, R.layout.item_event_content) {
    override fun bind(item: EventContentHolder.EventContent) {
        with(itemView) {
            tvContentHead.typeface = mediumTypeFace
            tvContentHead.text = item.text

            if (item.isSelected) {
                vTriangle.visibility = View.VISIBLE
                tvContentHead.alpha = 1f
            } else {
                vTriangle.visibility = View.INVISIBLE
                tvContentHead.alpha = .5f
            }

            vPoint.visibility = if (item.showPoint) View.VISIBLE else View.GONE
            setOnClickListener { onContentClickListener?.invoke(adapterPosition) }

            (layoutParams as RecyclerView.LayoutParams).setMargins(if (adapterPosition == 0) 18.dpToPx() else 0, 0, 0, 0)
        }
    }

    class EventContent(val text: String, var isSelected: Boolean = false, var showPoint: Boolean = true)
}