package org.zapomni.venturers.data.model.response

import org.zapomni.venturers.data.model.response.firebase.ThemeResponse

data class ChatResponse(val chat: InnerChatResponse,
                        val meetings: List<MeetingResponse>,
                        val poll: PollResponse?,
                        val quota: Int) {

    class InnerChatResponse(val id: String,
                            val lastMessageNumber: Int,
                            val dailyMessageQuota: Int,
                            val theme: ThemeResponse)
}