package org.zapomni.venturers.presentation.holder

import android.annotation.SuppressLint
import android.support.v4.app.FragmentManager
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_conductor.view.*
import org.zapomni.venturers.R
import org.zapomni.venturers.domain.model.Curator
import org.zapomni.venturers.extensions.loadImage
import org.zapomni.venturers.presentation.base.BaseViewHolder
import org.zapomni.venturers.presentation.dialog.CuratorDialog

class CuratorHolder(rootView: ViewGroup, private val fragmentManager: FragmentManager) : BaseViewHolder<Curator>(rootView, R.layout.item_conductor) {

    @SuppressLint("SetTextI18n")
    override fun bind(item: Curator) {
        with(itemView) {
            tvCount.typeface = regularTypeFace
            tvName.typeface = semiBoldTypeFace
            tvRating.typeface = regularTypeFace

            tvCount.text = adapterPosition.inc().toString()
            tvName.text = "${item.name} ${item.surname}"
            tvRating.text = "${item.rating} / ${item.likes}"
            imgAvatar.loadImage(item.image)

            rBar.rating = item.rating.toInt().toFloat()

            setOnClickListener { CuratorDialog.show(fragmentManager, item!!, false) }
        }
    }

}