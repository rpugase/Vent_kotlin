package org.zapomni.venturers.presentation.holder

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_poll.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.PollAnswer
import org.zapomni.venturers.presentation.base.BaseViewHolder

class PollAnswerHolder(rootView: ViewGroup) : BaseViewHolder<PollAnswer>(rootView, R.layout.item_poll) {
    @SuppressLint("SetTextI18n")
    override fun bind(item: PollAnswer) {
        with(itemView) {
            tvAnswer.typeface = regularTypeFace
            tvPercent.typeface = semiBoldTypeFace
            tvProgress.typeface = regularTypeFace

            tvAnswer.text = item.answer
            tvPercent.text = "${item.percent}%"
            progress.max = item.max
            progress.progress = item.count
            tvProgress.text = item.count.toString()

            if (item.win) {
                tvAnswer.typeface = semiBoldTypeFace
                imgWin.visibility = View.VISIBLE
            }
        }
    }
}