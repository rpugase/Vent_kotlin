package org.zapomni.venturers.presentation.chat.meet.newm

import android.text.format.DateUtils
import org.zapomni.venturers.domain.model.chat.Meet
import org.zapomni.venturers.presentation.base.BasePresenter
import java.util.*

class NewMeetPresenter : BasePresenter<NewMeetView>() {

    var dateOfMeeting = Calendar.getInstance()
    var feltDateOfMeeting = false

    fun makeMeet(name: String, place: String) {
        when {
            name.isBlank() -> view?.onNameError()
            place.isBlank() -> view?.onPlaceError()
            !feltDateOfMeeting -> view?.onDateError()
            dateOfMeeting.time.time < System.currentTimeMillis() + DateUtils.HOUR_IN_MILLIS -> view?.onErrorEarlyDate()
            else -> view?.returnWithTopic(Meet(name, place, dateOfMeeting.time))
        }
    }
}