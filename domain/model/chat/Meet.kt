package org.zapomni.venturers.domain.model.chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Meet(val name: String,
                val place: String,
                val time: Date = Date(),
                val id: String = "") : Parcelable