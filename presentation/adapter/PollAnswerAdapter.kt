package org.zapomni.venturers.presentation.adapter

import android.view.ViewGroup
import org.zapomni.venturers.domain.model.chat.PollAnswer
import org.zapomni.venturers.presentation.base.BaseAdapter
import org.zapomni.venturers.presentation.holder.PollAnswerHolder

class PollAnswerAdapter : BaseAdapter<PollAnswerHolder, PollAnswer>() {
    override fun getHolder(rootView: ViewGroup, viewType: Int) = PollAnswerHolder(rootView)
}