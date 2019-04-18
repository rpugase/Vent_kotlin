package org.zapomni.venturers.presentation.holder.chat.background

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_background_gallery.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.BackgroundModel
import org.zapomni.venturers.presentation.base.BaseViewHolder
import org.zapomni.venturers.presentation.chat.topic.background.BackgroundTopicPresenter

class BackgroundAddHolder(root: ViewGroup, private val presenter: BackgroundTopicPresenter) : BaseViewHolder<BackgroundModel>(root, R.layout.item_background_gallery) {

    override fun bind(item: BackgroundModel) {
        with(itemView) {
            tvAgenda.typeface = boldTypeFace
            tvTitle.typeface = regularTypeFace
            tvLearnMore.typeface = boldTypeFace

            setOnClickListener { presenter.loadPictureFromStorage() }
        }
    }
}