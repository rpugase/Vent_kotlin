package org.zapomni.venturers.domain.mapper

import com.google.firebase.firestore.DocumentChange
import io.reactivex.functions.Function
import org.zapomni.venturers.data.model.response.firebase.MessageFireResponse
import org.zapomni.venturers.data.repository.PreferencesRepository
import org.zapomni.venturers.domain.model.User
import org.zapomni.venturers.domain.model.chat.Message
import org.zapomni.venturers.extensions.getPhotoUrlById
import java.util.*

class MessageFireMapper(private val preferences: PreferencesRepository) : Function<MessageFireResponse, Message> {

    override fun apply(message: MessageFireResponse): Message {
        val me = preferences.user!!
        return Message(User(id = message.userId, name = message.name, surname = message.surname, phoneNumber = message.userPhone, image = message.userPic?.getPhotoUrlById()),
                id = message.id, text = message.text ?: "", meet = message.meeting.toDomainModel(),
                likes = message.likes.keys.size, likePressed = message.likes.containsKey(me.id),
                photoUrl = message.photo?.getPhotoUrlById(), time = Date(message.created), me = message.userId == me.id,
                chest = message.chest.toDomainModel(), onTopic = message.onTopic,
                changeType = if (message.messageType == "deleted") DocumentChange.Type.REMOVED else message.changeType,
                replyMessage = if (message.reply != null) Message(id = message.reply!!.messageId, user = User(name = message.reply?.name), text = message.reply?.text
                        ?: "") else null)
    }

}