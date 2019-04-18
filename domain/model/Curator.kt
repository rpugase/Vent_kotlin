package org.zapomni.venturers.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Curator(val name: String,
                   val surname: String,
                   val rating: Float,
                   val likes: Int,
                   val about: String,
                   val image: String? = null,
                   val phone: String?) : Parcelable