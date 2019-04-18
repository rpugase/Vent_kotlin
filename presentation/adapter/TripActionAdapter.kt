package org.zapomni.venturers.presentation.adapter

import android.view.ViewGroup
import org.zapomni.venturers.domain.model.TripAction
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.holder.TripActionHolder

class TripActionAdapter : BaseAdapter<TripActionHolder, TripAction>() {

    var onClickListener: ((position: Int) -> Unit)? = null

    override fun getHolder(rootView: ViewGroup, viewType: Int) = TripActionHolder(rootView, onClickListener)
}