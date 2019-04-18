package org.zapomni.venturers.domain.model.chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Poll(val id: String,
                val question: String,
                val answers: List<PollAnswer>,
                val photoId: String? = null,
                var voted: Boolean = false) : Parcelable