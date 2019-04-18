package org.zapomni.venturers.presentation.adapter

import android.view.ViewGroup
import org.zapomni.venturers.domain.model.Bonus
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.holder.BonusHolder

class BonusAdapter : BaseAdapter<BonusHolder, Bonus>() {
    override fun getHolder(rootView: ViewGroup, viewType: Int) = BonusHolder(rootView)
}