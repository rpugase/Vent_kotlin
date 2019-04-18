package org.zapomni.venturers.presentation.holder.chat.background

import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_background.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.chat.BackgroundModel
import org.zapomni.venturers.extensions.loadImageFromAssets
import org.zapomni.venturers.presentation.base.BaseViewHolder
import org.zapomni.venturers.presentation.chat.topic.background.BackgroundTopicPresenter

class BackgroundHolder(root: ViewGroup, private val presenter: BackgroundTopicPresenter) : BaseViewHolder<BackgroundModel>(root, R.layout.item_background) {

    override fun bind(item: BackgroundModel) {
        with(itemView) {
            tvAgenda.typeface = boldTypeFace
            tvTitle.typeface = regularTypeFace
            tvLearnMore.typeface = boldTypeFace

            imgBackground.loadImageFromAssets(item.id)

            setOnClickListener { presenter.takePicture(item) }
        }
    }
}