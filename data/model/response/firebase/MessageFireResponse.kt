package org.zapomni.venturers.data.model.response.firebase

import com.google.firebase.firestore.DocumentChange

data class MessageFireResponse(val id: String = "",
                               val changeType: DocumentChange.Type = DocumentChange.Type.ADDED,
                               val userId: String = "",
                               val userPic: String? = "",
                               val text: String? = "",
                               val photo: String? = null,
                               val meeting: MeetingFireResponse? = null,
                               val created: Long = System.currentTimeMillis(),
                               val likes: Map<String, Boolean> = mapOf(),
                               val name: String? = "",
                               val surname: String? = "",
                               val userPhone: String? = "",
                               val chest: ChestFireResponse? = null,
                               val onTopic: Boolean = false,
                               val messageType: String? = null,
                               var reply: ReplyMessageFireResponse? = null)