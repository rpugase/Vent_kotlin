package org.zapomni.venturers.domain.model.chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Agenda(val theme: String,
                  val description: String,
                  val photoId: String? = null) : Parcelable