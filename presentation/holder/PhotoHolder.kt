package org.zapomni.venturers.presentation.holder

import android.view.ViewGroup
import com.facebook.drawee.view.SimpleDraweeView
import org.zapomni.venturers.R
import org.zapomni.venturers.presentation.base.BaseViewHolder

class PhotoHolder(rootView: ViewGroup, private val onImageClickListener: ((position: Int) -> Unit)? = null) :
        BaseViewHolder<String>(rootView, R.layout.item_photo) {
    override fun bind(item: String) {
        (itemView as SimpleDraweeView).setImageURI(item)
        itemView.setOnClickListener { onImageClickListener?.invoke(adapterPosition) }
    }
}