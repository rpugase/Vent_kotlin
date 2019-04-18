package org.zapomni.venturers.presentation.adapter

import android.view.ViewGroup
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.holder.PhotoHolder

class PhotoAdapter : BaseAdapter<PhotoHolder, String>() {

    var onImageClickListener: ((position: Int) -> Unit)? = null

    override fun getHolder(rootView: ViewGroup, viewType: Int) = PhotoHolder(rootView, onImageClickListener)
}