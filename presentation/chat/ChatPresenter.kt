package org.zapomni.venturers.presentation.chat

import com.google.firebase.firestore.DocumentChange
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import org.zapomni.venturers.data.repository.FirebaseRepository
import org.zapomni.venturers.domain.ChatInteractor
import org.zapomni.venturers.domain.UserInteractor
import org.zapomni.venturers.domain.exception.BanForDayException
import org.zapomni.venturers.domain.exception.BanPermanentException
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.domain.model.chat.*
import org.zapomni.venturers.presentation.base.BasePresenter
import java.io.File

class ChatPresenter(private val chatInteractor: ChatInteractor,
                    private val userInteractor: UserInteractor) : BasePresenter<ChatView>() {

    var onTopic: Boolean = false
    var imgFile: File? = null
    var meet: Meet? = null
    var replyMessage: Message? = null

    var dailyMessageQuota = 5
    var sentMessageCount = 0
        set(value) {
            field = value
            view?.loadAvailableMessages(value, dailyMessageQuota)
            view?.showAnswerButton(dailyMessageQuota == 0 || value != dailyMessageQuota) // todo change to -1
        }

    private var chat: Chat? = null
    private var loadingPaginationList = false
    private var messageDisposable = Disposables.disposed().apply { disposable = this }
    private var banReason: String? = null
    private var isAdmin = false

    override fun attachView(view: ChatView) {
        super.attachView(view)
        chatInteractor.loadChat()
                .execute({
                    setChat(it)
                    userInteractor.getUser().execute({
                        isAdmin = it.admin
                        if (it.admin) {
                            view.showTopicButton()
                        }
                    })
                    view.setVisibilitySwapChat(chatInteractor.getChatsCount() != 0)
                })

        if (banReason != null) {
            view.doOnBanForDay(banReason!!)
        }

        // todo set onTopic, imgFile and Meet
    }

    override fun detachView() {
        chatInteractor.onMeetingListener = null
        super.detachView()
    }

    private fun setChat(chat: Chat) {
        this.chat = chat
        dailyMessageQuota = chat.dailyMessageQuota
        sentMessageCount = chat.sentMessagesToday

        view?.clearChat()
        if (chat.poll != null) view?.loadPoll(chat.poll!!) else view?.hidePoll()
        if (chat.poll == null) if (chat.agenda != null) view?.loadAgenda(chat.agenda!!) else view?.hideAgenda()
        if (chat.id != "chat-global" && chat.agenda != null) view?.loadAgendaForEvent(chat.agenda!!) // todo replace to theme of chat
        if (chat.meets.isNotEmpty()) view?.loadMeets(chat.meets) else view?.hideMeets()
        if (chat.messages.isNotEmpty()) view?.loadChat(chat.messages)

        if (chatInteractor.startPagination) {
            view?.startPagination()
        }

        if (!messageDisposable.isDisposed) messageDisposable.dispose()
        messageDisposable = chatInteractor.listenMessages(chat.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.changeType) {
                        DocumentChange.Type.ADDED -> view?.addMessage(it)
                        DocumentChange.Type.REMOVED -> view?.removeMessage(it)
                        DocumentChange.Type.MODIFIED -> view?.changeMessage(it)
                    }
                }, { view?.onError(it) })
        chatInteractor.onMeetingListener = { _ -> view?.loadMeets(chat.meets) }
    }

    fun sendMessage(text: String) {
        if (text.isNotBlank() || imgFile != null || meet != null) {
            view?.onStartSendingMessage()
            chatInteractor.sendMessage(text, onTopic, imgFile, meet, replyMessage)
                    .execute({
                        if (meet != null) view?.animateMeet()

                        imgFile = null
                        meet = null
                        replyMessage = null
                        view?.onFinishSendingMessage(true); sentMessageCount++
                    }, {
                        if (it is BanForDayException) {
                            banReason = it.localizedMessage
                            view?.doOnBanForDay(banReason!!)
                        } else if (it is BanPermanentException) {
                            if (chatInteractor.fakeMessage != null) {
                                view?.addMessage(chatInteractor.fakeMessage!!)
                            }
                        }
                        view?.onFinishSendingMessage(false)
                        view?.onError(it)
                    }, withLoading = false)
        }
    }

    fun loadMessages(message: Message) {
        if (!loadingPaginationList) {
            loadingPaginationList = true
            chatInteractor.loadMessages(message)
                    .execute({
                        loadingPaginationList = false
                        view?.addMessages(it)
                        if (it.size < FirebaseRepository.STEP_PAGINATION_VALUE) {
                            chatInteractor.startPagination = false
                            view?.stopPagination()
                        }
                    })
        }
    }

    fun doOnLike(messageId: String, like: Boolean) {
        chatInteractor.makeLike(messageId, like)
    }

    fun onUserShow(user: User) {
        view?.showUser(user, chat?.userIdSawPhone == null, user.id == chat?.userIdSawPhone)
    }

    fun onShowPhone(user: User) {
        chatInteractor.doOnShowPhone(user)
    }

    fun openChest(indexOf: Int, chestId: String) {
        chatInteractor.openChest(chestId).execute({
            //            view?.updateChest(indexOf, chat!!.messages[indexOf].copy(chest = chat!!.messages[indexOf].chest?.copy(winner = it)))
        })
    }

    fun openPoll() {
        if (chat?.poll != null) {
            view?.showPoll(chat!!.poll!!)
        }
    }

    fun sendPollAnswer(answer: PollAnswer) {
        chatInteractor.sendPollAnswer(answer)
    }

    fun onMessageAction(message: Message) {
        userInteractor.getUser().execute({
            view?.onMessageAction(message, message.user.id != it.id, isAdmin)
        })
    }

    fun replyMessage(message: Message) {
        replyMessage = message.copy()
        view?.showReply(message)
    }

    fun removeMessage(message: Message) {
        chatInteractor.removeMessage(message).execute({ view?.removeMessage(message) })
    }

    fun banForDay(user: User, reason: String) {
        chatInteractor.banForDay(user, reason).execute({})
    }

    fun banPermanent(user: User) {
        chatInteractor.banPermanent(user).execute({})
    }

    fun savePoll(poll: Poll) {
        chat?.poll = poll
        view?.loadPoll(poll)
    }

    fun saveAgenda(agenda: Agenda) {
        chat?.agenda = agenda
        view?.loadAgenda(agenda)
    }

    fun swapChat() {
        chatInteractor.replaceChat().execute(this::setChat)
    }

    fun openPhoto(photoUrl: String) {
        view?.openPhoto(photoUrl)
    }

    fun showTip() {
        if (chat?.showTips == true) {
            view?.showTip()
            chatInteractor.userTrained()
        }
    }

    fun showReplyMessage(messageId: String) {
        chatInteractor.getMessage(messageId).execute({ view?.showReplyMessage(it) })
    }

    fun removeTopic() {
        chatInteractor.removeTopic().execute({
            view?.removeTopic()
        })
    }
}