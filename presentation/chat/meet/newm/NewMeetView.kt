package org.zapomni.venturers.presentation.chat.meet.newm

import org.zapomni.venturers.domain.model.chat.Meet
import org.zapomni.venturers.presentation.base.BaseView

interface NewMeetView : BaseView {

    fun returnWithTopic(meet: Meet)

    fun onNameError()
    fun onPlaceError()
    fun onDateError()
    fun onErrorEarlyDate()
}