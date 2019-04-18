package org.zapomni.venturers.domain.model.chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhoneSettings(val canSee: Boolean = true,
                         val userId: String? = null) : Parcelable