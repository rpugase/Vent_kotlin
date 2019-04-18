package org.zapomni.venturers.presentation.base

import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.zapomni.venturers.R

abstract class BaseViewHolder<in T>(root: ViewGroup, layoutId: Int) : RecyclerView.ViewHolder(LayoutInflater.from(root.context).inflate(layoutId, root, false)) {

    protected var boldTypeFace: Typeface? = ResourcesCompat.getFont(itemView.context, R.font.gotha_pro_bold)
        private set
    protected var mediumTypeFace: Typeface? = ResourcesCompat.getFont(itemView.context, R.font.gotha_pro_medium)
        private set
    protected var regularTypeFace: Typeface? = ResourcesCompat.getFont(itemView.context, R.font.source_sans_pro_regular)
        private set
    protected var semiBoldTypeFace: Typeface? = ResourcesCompat.getFont(itemView.context, R.font.source_sans_pro_semi_bold)
        private set
    protected var italicTypeFace: Typeface? = ResourcesCompat.getFont(itemView.context, R.font.source_sans_pro_italic)
        private set

    protected val defaultRecyclable = true

    abstract fun bind(item: T)
}