package org.zapomni.venturers.domain

import com.google.firebase.firestore.DocumentChange
import com.google.gson.JsonObject
import id.zelory.compressor.Compressor
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.zapomni.venturers.data.model.BaseResponse
import org.zapomni.venturers.data.model.response.ChatResponse
import org.zapomni.venturers.data.model.response.firebase.MessageFireResponse
import org.zapomni.venturers.data.repository.*
import org.zapomni.venturers.domain.exception.ApiException
import org.zapomni.venturers.domain.exception.BanForDayException
import org.zapomni.venturers.domain.exception.BanPermanentException
import org.zapomni.venturers.domain.exception.MeetingTooLateException
import org.zapomni.venturers.domain.mapper.ChatListMapper
import org.zapomni.venturers.domain.mapper.MessageFireMapper
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.domain.model.chat.*
import org.zapomni.venturers.extensions.formatForCompareDate
import org.zapomni.venturers.extensions.getPhotoUrlById
import org.zapomni.venturers.extensions.toMultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class ChatInteractor(private val preferences: PreferencesRepository,
                     private val firebaseRepository: FirebaseRepository,
                     private val chatRepository: ChatRepository,
                     private val messageFireMapper: MessageFireMapper,
                     private val chatListMapper: ChatListMapper,
                     private val fileRepository: FileRepository,
                     private val assetsRepository: AssetsRepository,
                     private val userRepository: UserRepository,
                     private val compressor: Compressor) {

    var startPagination = false

    companion object {
        const val MAIN_CHAT = "chat-global"
    }

    private val dates = mutableMapOf<String, MutableList<Date>>()
    private val chats = mutableMapOf<String, Chat>()
    private var currentChatId: String = MAIN_CHAT
    private var chatsIterator: MutableIterator<String>? = null
    private val actualUsers = mutableListOf<User>()

    var onMeetingListener: ((meet: Meet) -> Unit)? = null
    var fakeMessage: Message? = null
        private set

    fun loadChat(chatId: String? = null): Single<Chat> {
        val oldChatId = currentChatId
        if (chatId != null) currentChatId = chatId

        if (chats[currentChatId] != null) return Single.just(chats[currentChatId])
        else return Single.zip<BaseResponse<List<ChatResponse>>, List<MessageFireResponse>, Chat>(chatRepository.getFullChats(), firebaseRepository.getFirstMessages(currentChatId), BiFunction { chatResponse, messages ->
            val chatsData = chatListMapper.apply(chatResponse.data ?: listOf())

            chatsData.forEach { chats[it.id] = it }
            chatsIterator = chats.keys.iterator()

            val chat = chatsData.first()
            currentChatId = chat.id
            chat.messages.addAll(messages.map(messageFireMapper::apply).toMutableList().setShowAvatar().withMe(preferences.user!!).addDates())

            dates[currentChatId] = mutableListOf(chat.messages.firstOrNull()?.time ?: Date())
            startPagination = chat.messages.size.toLong() == FirebaseRepository.START_PAGINATION_VALUE

            chats.forEach {
                val photoId = it.value.agenda?.photoId
                if (photoId != null && photoId.contains(Regex("(.png|.jpg|.jpeg)")).not()) {
                    it.value.agenda = it.value.agenda?.copy(photoId = "$photoId.png")
                }
            }

            if (chatId != null) currentChatId = oldChatId
            return@BiFunction chats[chatsIterator!!.next()]!!
        })
    }

    fun getChatsCount() = chats.size

    fun replaceChat(): Single<Chat> {
        if (chatsIterator?.hasNext()?.not() == true) {
            chatsIterator = chats.keys.iterator()
        }
        val chatId = chatsIterator?.next()
        if (chatId != null) {
            currentChatId = chatId
        }

        if (chats[currentChatId]?.messages?.isEmpty() == true) {
            val chat = chats[currentChatId]
            currentChatId = chat!!.id
            return firebaseRepository.getFirstMessages(currentChatId)
                    .map {
                        chat.messages.addAll(it.map(messageFireMapper::apply)
                                .toMutableList().setShowAvatar().withMe(preferences.user!!).addDates())
                        dates[currentChatId] = mutableListOf(chat.messages.firstOrNull()?.time
                                ?: Date())
                        startPagination = chat.messages.size.toLong() == FirebaseRepository.START_PAGINATION_VALUE
                        chats[currentChatId]
                    }
        } else return Single.just(chats[currentChatId]!!)
    }

    fun listenMessages(chatId: String): Flowable<Message> {
        return firebaseRepository.listenMessages(chatId)
                .map(messageFireMapper)
                .map {
                    val indexOf = chats[currentChatId]?.messages!!.indexOfFirst { msg -> it.id == msg.id }
                    var messageResult = it.resolveUser()
                    when {
                        it.changeType == DocumentChange.Type.ADDED -> {
                            if (chats[currentChatId]!!.messages.isNotEmpty()) {
                                it.showAvatar = chats[currentChatId]!!.messages.last().user.id != it.user.id
                            }
                            chats[currentChatId]?.messages?.add(messageResult)
                        }
                        it.changeType == DocumentChange.Type.MODIFIED -> {
                            if (indexOf != -1) {
                                messageResult = chats[currentChatId]?.messages!![indexOf]
                                        .copy(likes = it.likes, text = it.text, chest = it.chest, changeType = it.changeType)
                                chats[currentChatId]?.messages!![indexOf] = messageResult
                            }
                        }
                        it.changeType == DocumentChange.Type.REMOVED -> {
                            if (indexOf != -1) {
                                chats[currentChatId]?.messages?.removeAt(indexOf)
                                chats[currentChatId]?.meets?.removeAll { meet -> meet.id == it.meet?.id }
                            }
                        }
                    }

                    if (it.meet != null && it.changeType != DocumentChange.Type.REMOVED) {
                        chats[currentChatId]?.meets?.add(0, it)
                        onMeetingListener?.invoke(it.meet)
                    }

                    messageResult
                }
    }

    fun loadMessages(message: Message): Single<List<Message>> {
        return firebaseRepository.getMessagesFromId(currentChatId, message.time.time)
                .map {
                    it.map(messageFireMapper::apply)
                            .toMutableList().setShowAvatar().withMe(preferences.user!!).addDates().resolveUsers()
                            .also { chats[currentChatId]?.messages?.addAll(0, it) }
                }
    }

    fun sendMessage(text: String, onTopic: Boolean = false, photoFile: File? = null,
                    meet: Meet? = null, replyMessage: Message?): Completable {
        return Completable.create {
            val messageResponse: Response<BaseResponse<JsonObject>>?
            var photoId: String? = null

            if (meet != null) {
                messageResponse = chatRepository.sendMeetingSync(
                        currentChatId, meet.name, meet.place, meet.time.time, onTopic).execute()

                if (messageResponse.code() == 406) it.onError(MeetingTooLateException())

            } else {
                if (photoFile != null) {
                    val fileResponse = fileRepository.uploadFileSync(compressor.compressToFile(photoFile).toMultipartBody("image/*")).execute()
                    photoId = fileResponse.body()?.data
                }

                messageResponse = chatRepository.sendMessageSync(currentChatId, text, onTopic, photoId, replyMessage?.id).execute()
            }

            if (messageResponse.code() == 802) {
                it.onError(BanForDayException(JSONObject(messageResponse.errorBody()?.string()).getString("message")))
            } else if (messageResponse.code() == 801) {
                fakeMessage = Message(user = preferences.user!!, text = text, onTopic = onTopic,
                        photoUrl = photoId?.getPhotoUrlById(), meet = meet, replyMessage = replyMessage)
                chats[currentChatId]?.messages?.add(fakeMessage!!)
                chats[currentChatId]!!.sentMessagesToday++
                it.onError(BanPermanentException())
            }

            chats[currentChatId]!!.sentMessagesToday++
            it.onComplete()
        }
    }

    fun removeMessage(message: Message): Completable {
        return chatRepository.deleteMessage(currentChatId, message.id)
    }

    fun banForDay(user: User, reason: String): Completable {
        return chatRepository.banTemp(currentChatId, user.id!!)
    }

    fun banPermanent(user: User): Completable {
        return chatRepository.banPermanent(currentChatId, user.id!!)
    }

    fun createAgenda(title: String, description: String, backgroundId: String? = null, backgroundFile: File? = null): Single<Agenda> {
        return Single.create {
            val agenda = Agenda(title, description)
            var backId = backgroundId
            if (backgroundFile != null) {
                val response = fileRepository.uploadFileSync(compressor.compressToFile(backgroundFile).toMultipartBody("image/*")).execute()
                backId = response.body()?.data
            }

            val agendaResponse = chatRepository.createAgendaSync(currentChatId, title, backId, description).execute()
            if (agendaResponse.isSuccessful) {
                val resultAgenda = agenda.copy(photoId = backId)
                chats[currentChatId]?.agenda = resultAgenda
                it.onSuccess(resultAgenda)
            } else it.onError(ApiException(agendaResponse.errorBody()?.string()))
        }
    }

    fun createPoll(title: String, answerOptions: List<String>, backgroundId: String? = null, backgroundFile: File? = null): Single<Poll> {
        return Single.create {
            val poll = Poll("0", title, answerOptions.mapIndexed { index, answer -> PollAnswer(index.toString(), answer, 0) })
            var backId = backgroundId?.dropLast(4)
            if (backgroundFile != null) {
                val response = fileRepository.uploadFileSync(compressor.compressToFile(backgroundFile).toMultipartBody("image/*")).execute()
                backId = response.body()?.data
            }

            val pollResponse = chatRepository.createPoll(currentChatId, title, answerOptions.toTypedArray(), backId).execute()
            if (pollResponse.isSuccessful) {
                val resultPoll = poll.copy(photoId = backId, id = pollResponse.body()!!.data!!.id)
                chats[currentChatId]?.poll = resultPoll
                it.onSuccess(resultPoll)
            } else it.onError(ApiException(pollResponse.errorBody()?.string()))
        }
    }

    fun sendPollAnswer(answer: PollAnswer) {
        chats[currentChatId]?.poll?.apply {
            voted = true
            val answerInModel = answers.first { it.answer == answer.answer }
            answerInModel.count++

            val maxAnswer = answers.sumBy { it.count }
            val winner = answers.maxBy { it.count }
            answerInModel.fillData(maxAnswer, winner)

            chatRepository.votePollSync(id, answer.answer).enqueue(object : Callback<BaseResponse<JsonObject>> {
                override fun onResponse(call: Call<BaseResponse<JsonObject>>?, response: Response<BaseResponse<JsonObject>>?) {}
                override fun onFailure(call: Call<BaseResponse<JsonObject>>?, t: Throwable?) {}
            })
        }
    }


    fun makeLike(messageId: String, like: Boolean) {
        chatRepository.makeLike(currentChatId, messageId, like)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.isSuccessful) {
                        val message = chats[currentChatId]?.messages?.find { it.id == messageId }
                        message?.likePressed = like
                        if (like) message?.likes?.inc() else message?.likes?.dec()

                        val meet = chats[currentChatId]?.meets?.find { it.id == messageId }
                        meet?.likePressed = like
                        if (like) meet?.likes?.inc() else meet?.likes?.dec()
                    }
                }, {})
    }

    fun doOnShowPhone(user: User) {
        chats[currentChatId]?.userIdSawPhone = user.id
        // todo request show phone
    }

    fun openChest(chestId: String): Completable {
        return Completable.create {
            val chestResponse = chatRepository.grabChestSync(chestId).execute()
            it.onComplete()
//            if (chestResponse.isSuccessful) {
//                val winnerId = chestResponse.body()?.data?.winnerId
//                if (winnerId != null) {
//                    val userResponse = userRepository.getUserByIdSync(winnerId).execute()
//                    if (userResponse.isSuccessful) {
//                        val user = userResponse.body()?.data.toDomainModel()
//                        if (user != null) {
//                            it.onSuccess(user)
//                        } else it.onError(ApiException(userResponse.body()?.message))
//                    } else it.onError(ApiException(userResponse.body()?.message))
//                } else it.onError(ApiException(chestResponse.body()?.message))
//            } else it.onError(ApiException(chestResponse.body()?.message))
        }
    }

    fun loadBackgrounds(): Single<List<BackgroundModel>> {
        return assetsRepository.getBackgrounds()
    }

    fun userTrained() {
        preferences.showTips = false
        chats.values.forEach { it.showTips = false }
    }

    fun getMessage(id: String) = firebaseRepository.getMessage(currentChatId, id)
            .map(messageFireMapper::apply)

    fun removeTopic(): Completable {
        val chat = chats[currentChatId]

        if (chat?.poll != null) {
            chat.poll = null
        } else if (chat?.agenda != null) {
            chat.agenda = null
        }

        return Completable.complete()
    }

    private fun MutableList<Message>.withMe(me: User): MutableList<Message> {
        forEach { it.me = it.user.id == me.id }
        return this
    }

    private fun MutableList<Message>.setShowAvatar(): MutableList<Message> {
        var user = User()
        val tempMessageList = mutableListOf(*chats[currentChatId]?.messages?.toTypedArray()
                ?: arrayOf())
        tempMessageList.addAll(0, this)

        tempMessageList.forEachIndexed { index, message ->
            when {
                index == 0 -> this[index].showAvatar = true
                index < this.size -> this[index].showAvatar = message.user.id != user.id
                else -> chats[currentChatId]?.messages!![index - this.size].showAvatar = message.user.id != user.id
            }

            user = message.user
        }

        return this
    }

    private fun MutableList<Message>.addDates(): MutableList<Message> {

        if (dates[currentChatId]?.isEmpty() != false) dates[currentChatId]?.add(this[0].time)
        val messagesResult = mutableListOf<Message>()

        forEachIndexed { index, message ->
            if (dates[currentChatId]?.none { it.formatForCompareDate() == message.time.formatForCompareDate() } == true) {
                dates[currentChatId]?.add(message.time)
                messagesResult.add(Message(time = this[index].time))
            }
            messagesResult.add(message)
        }

        return messagesResult
    }

    private fun Message.resolveUser() = this.copy(user = actualUsers.find { it.id == user.id }
            ?: user.apply { actualUsers.add(this) })

    private fun MutableList<Message>.resolveUsers() = map { it.resolveUser() }

}