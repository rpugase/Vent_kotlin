package org.zapomni.venturers.data.repository

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Single
import org.zapomni.venturers.data.model.response.firebase.MessageFireResponse

class FirebaseRepository(private val firestore: FirebaseFirestore) {

    companion object {
        val START_PAGINATION_VALUE = 20L
        val STEP_PAGINATION_VALUE = 10L
    }

    private var canWriteMessage: Boolean = false
    private var listener: ListenerRegistration? = null

    fun getFirstMessages(chatId: String): Single<List<MessageFireResponse>> {
        return Single.create { emitter ->
            firestore.collection(chatId)
                    .orderBy("created", Query.Direction.DESCENDING)
                    .limit(START_PAGINATION_VALUE)
                    .get()
                    .addOnSuccessListener {
                        val docs = it.documents
                        if (docs.isEmpty()) {
                            emitter.onSuccess(listOf())
                        } else {
                            emitter.onSuccess(it.documents.map {
                                it.toObject(MessageFireResponse::class.java)?.copy(id = it.id)!!
                            }.asReversed().filter { it.messageType != "deleted" })
                        }
                    }
        }
    }

    fun getMessagesFromId(chatId: String, startFromDate: Long): Single<List<MessageFireResponse>> {
        return Single.create { emitter ->
            firestore.collection(chatId)
                    .orderBy("created", Query.Direction.DESCENDING)
                    .startAfter(startFromDate)
                    .limit(STEP_PAGINATION_VALUE)
                    .get()
                    .addOnSuccessListener {
                        emitter.onSuccess(it.documents.map {
                            it.toObject(MessageFireResponse::class.java)?.copy(id = it.id)!!
                        }.asReversed().filter { it.messageType != "deleted" })
                    }
        }
    }

    fun listenMessages(chatId: String): Flowable<MessageFireResponse> {
        return Flowable.create<MessageFireResponse>({
            canWriteMessage = false
            listener = firestore.collection(chatId)
                    .addSnapshotListener { snapshot, error ->
                        if (!canWriteMessage) {
                            canWriteMessage = true
                            return@addSnapshotListener
                        }

                        for (dc: DocumentChange in snapshot!!.documentChanges) {
                            it.onNext(dc.document.toObject(MessageFireResponse::class.java)
                                    .copy(id = dc.document.id, changeType = dc.type))
                        }

                        if (error != null) {
                            it.onError(error)
                        }
                    }

        }, BackpressureStrategy.BUFFER)
                .doOnTerminate { listener?.remove(); listener = null }
    }

    fun getMessage(chatId: String, messageId: String) = Single.create<MessageFireResponse> { emitter ->
        firestore.collection(chatId)
                .document(messageId)
                .get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val result = it.result
                        emitter.onSuccess(result.toObject(MessageFireResponse::class.java)!!.copy(id = result.id))
                    }
                }
    }

    private fun String.getCountFromId() = split("-").last().toInt()
}