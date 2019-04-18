package org.zapomni.venturers.domain.mapper

import io.reactivex.functions.Function
import org.zapomni.venturers.data.model.response.ChatResponse
import org.zapomni.venturers.data.repository.PreferencesRepository
import org.zapomni.venturers.domain.model.chat.Chat

class ChatListMapper(private val meetingListMapper: MeetingListMapper,
                     private val preferences: PreferencesRepository) : Function<List<ChatResponse>, List<Chat>> {

    override fun apply(chats: List<ChatResponse>): List<Chat> {
        return chats.map {
            Chat(id = it.chat.id, sentMessagesToday = it.chat.dailyMessageQuota - it.quota, dailyMessageQuota = it.chat.dailyMessageQuota,
                    poll = it.poll.toDomainModel(), meets = meetingListMapper.apply(it.meetings).toMutableList(),
                    agenda = it.chat.theme.toDomainModel(), showTips = preferences.showTips, usersCount = 365) // todo get users count from the server
        }
    }
}