package org.zapomni.venturers.presentation.adapter

import android.view.ViewGroup
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.base.BaseViewHolder
import org.zapomni.venturers.presentation.chat.meet.list.MeetsPresenter
import org.zapomni.venturers.presentation.holder.MeetHolder
import org.zapomni.venturers.presentation.holder.MeetTopHolder

class MeetAdapter(private val presenter: MeetsPresenter) : BaseAdapter<BaseViewHolder<Message>, Message>() {

    private val VT_TOP = 0
    private val VT_MEET = 1

    var onBackButtonClickListener: (() -> Unit)? = null

    override fun getHolder(rootView: ViewGroup, viewType: Int) = when (viewType) {
        VT_TOP -> MeetTopHolder(rootView, onBackButtonClickListener)
        VT_MEET -> MeetHolder(rootView, presenter)
        else -> throw IllegalArgumentException("No such view type")
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Message>, position: Int) {
        if (position == 0) holder.bind(Message())
        else holder.bind(items[position - 1])
    }

    override fun getItemCount() = items.size + 1

    override fun getItemViewType(position: Int) = if (position == 0) VT_TOP else VT_MEET
}