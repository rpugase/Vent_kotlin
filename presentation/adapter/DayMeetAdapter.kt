package org.zapomni.venturers.presentation.adapter

import android.view.ViewGroup
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.holder.DayMeetHolder

class DayMeetAdapter : BaseAdapter<DayMeetHolder, String>() {
    override fun getHolder(rootView: ViewGroup, viewType: Int) = DayMeetHolder(rootView)
}