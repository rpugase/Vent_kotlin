package org.zapomni.venturers.domain.model.chat

import android.os.Parcelable
import com.google.firebase.firestore.DocumentChange
import kotlinx.android.parcel.Parcelize
import org.zapomni.venturers.domain.model.User
import java.util.*

@Parcelize
data class Message(val user: User = User(),
                   val text: String = "",
                   var likes: Int = 0,
                   var likePressed: Boolean = true,
                   val onTopic: Boolean = false,
                   var me: Boolean = false,
                   var showAvatar: Boolean = true,
                   val time: Date = Date(),
                   val photoUrl: String? = null,
                   val meet: Meet? = null,
                   val chest: Chest? = null,
                   val id: String = "",
                   val replyMessage: Message? = null,
                   val changeType: DocumentChange.Type = DocumentChange.Type.ADDED) : Parcelable