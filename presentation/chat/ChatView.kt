package org.zapomni.venturers.presentation.chat

import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.domain.model.chat.Agenda
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.domain.model.chat.Poll
import org.zapomni.venturers.presentation.base.BaseView

interface ChatView : BaseView {

    fun showMessageBar()
    fun showFab()
    fun showAnswerButton(show: Boolean)
    fun showUser(user: User, canSeePhone: Boolean, showPhone: Boolean)
    fun showPoll(poll: Poll)
    fun showReply(message: Message)
    fun showTopicButton()
    fun showTip()
    fun showReplyMessage(message: Message)
    fun setVisibilitySwapChat(show: Boolean)

    fun clearChat()

    fun updateChest(index: Int, message: Message)
    fun addMessage(message: Message)
    fun addMessages(messages: List<Message>)
    fun removeMessage(message: Message)
    fun changeMessage(message: Message)

    fun startPagination()
    fun stopPagination()

    fun loadChat(messages: List<Message>)
    fun loadMeets(meets: List<Message>)
    fun loadPoll(poll: Poll)
    fun loadAgenda(agenda: Agenda)
    fun loadAgendaForEvent(agenda: Agenda)
    fun loadAvailableMessages(current: Int, max: Int)
    fun removeTopic()

    fun hidePoll()
    fun hideAgenda()
    fun hideMeets()

    fun onStartSendingMessage()
    fun onFinishSendingMessage(allRight: Boolean)
    fun onMessageAction(message: Message, showPhone: Boolean, admin: Boolean)

    fun openPhoto(photoUrl: String)

    fun doOnBanForDay(reason: String)

    fun animateMeet()
}