package org.zapomni.venturers.presentation.adapter

import android.view.ViewGroup
import org.zapomni.venturers.domain.model.chat.BackgroundModel
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.base.BaseViewHolder
import org.zapomni.venturers.presentation.chat.topic.background.BackgroundTopicPresenter
import org.zapomni.venturers.presentation.holder.chat.background.BackgroundAddHolder
import org.zapomni.venturers.presentation.holder.chat.background.BackgroundHolder

class BackgroundAdapter(private val presenter: BackgroundTopicPresenter) : BaseAdapter<BaseViewHolder<BackgroundModel>, BackgroundModel>() {

    private val TYPE_SIMPLE = 0
    private val TYPE_ADD = 1

    override fun getHolder(rootView: ViewGroup, viewType: Int): BaseViewHolder<BackgroundModel> {
        return when (viewType) {
            TYPE_SIMPLE -> BackgroundHolder(rootView, presenter)
            TYPE_ADD -> BackgroundAddHolder(rootView, presenter)
            else -> throw IllegalArgumentException("Holder for viewType '$viewType' not found")
        }
    }

    override fun getItemCount() = items.size + 1

    override fun onBindViewHolder(holder: BaseViewHolder<BackgroundModel>, position: Int) {
        holder.bind(if (position < items.size) items[position] else BackgroundModel())
    }

    override fun getItemViewType(position: Int) = if (position < items.size) TYPE_SIMPLE else TYPE_ADD
}