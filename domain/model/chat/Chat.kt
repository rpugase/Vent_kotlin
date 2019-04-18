package org.zapomni.venturers.domain.model.chat

data class Chat(val id: String,
                var agenda: Agenda? = null,
                var poll: Poll? = null,
                var sentMessagesToday: Int = 0,
                val dailyMessageQuota: Int = 10,
                var userIdSawPhone: String? = null,
                var messages: MutableList<Message> = mutableListOf(),
                val meets: MutableList<Message> = mutableListOf(),
                var showTips: Boolean,
                val usersCount: Int)