package org.zapomni.venturers.presentation.holder

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_bonus.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Bonus
import org.zapomni.venturers.extensions.formatWithSimpleDate
import org.zapomni.venturers.presentation.base.BaseViewHolder

class BonusHolder(root: ViewGroup) : BaseViewHolder<Bonus>(root, R.layout.item_bonus) {

    override fun bind(item: Bonus) {
        with(itemView) {
            tvSumDate.typeface = semiBoldTypeFace
            tvPromoCode.typeface = boldTypeFace

            tvSumDate.text = context.getString(R.string.cash_with_date, item.price, item.date.formatWithSimpleDate())
            tvPromoCode.text = item.code
        }
    }
}