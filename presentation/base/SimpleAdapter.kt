package org.zapomni.venturers.presentation.base

import android.view.ViewGroup

class SimpleAdapter<H : BaseViewHolder<T>, T>(private val clazz: Class<H>) : BaseAdapter<H, T>() {
    override fun getHolder(rootView: ViewGroup, viewType: Int) = clazz.getConstructor(ViewGroup::class.java).newInstance(rootView)
}