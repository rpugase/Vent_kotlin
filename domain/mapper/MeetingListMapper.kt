package org.zapomni.venturers.domain.mapper

import io.reactivex.functions.Function
import org.zapomni.venturers.data.model.response.MeetingResponse
import org.zapomni.venturers.domain.model.chat.Meet
import org.zapomni.venturers.domain.model.chat.Message
import java.util.*

class MeetingListMapper : Function<List<MeetingResponse>, List<Message>> {
    override fun apply(meetings: List<MeetingResponse>): List<Message> {
        return meetings.map {
            Message(user = it.creator.toDomainModel()!!, id = it.id,
                    meet = Meet(it.name, it.meetingPoint, Date(it.meetingDate), it.id))
        }
    }
}