package org.zapomni.venturers.presentation.holder

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_trip_action.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.TripAction
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.presentation.base.BaseViewHolder

class TripActionHolder(rootView: ViewGroup,
                       private val onClickListener: ((position: Int) -> Unit)?) : BaseViewHolder<TripAction>(rootView, R.layout.item_trip_action) {
    override fun bind(item: TripAction) {
        with(itemView) {
            tvAction.typeface = mediumTypeFace

            imgIcon.loadImage(item.icon)
            tvAction.text = item.name

            setOnClickListener { onClickListener?.invoke(adapterPosition) }
        }
    }
}