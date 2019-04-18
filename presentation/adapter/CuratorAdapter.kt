package org.zapomni.venturers.presentation.adapter

import android.support.v4.app.FragmentManager
import android.view.ViewGroup
import org.zapomni.venturers.domain.model.Curator
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.holder.CuratorHolder


class CuratorAdapter(private val fragmentManager: FragmentManager) : BaseAdapter<CuratorHolder, Curator>() {
    override fun getHolder(rootView: ViewGroup, viewType: Int) = CuratorHolder(rootView, fragmentManager)
}