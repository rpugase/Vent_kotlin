package org.zapomni.venturers.data.model.response

data class MeetingResponse(val id: String,
                           val name: String,
                           val meetingPoint: String,
                           val meetingDate: Long,
                           val chatId: String,
                           val creator: UserResponse)