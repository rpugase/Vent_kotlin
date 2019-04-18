package org.zapomni.venturers.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Bonus(val code: String,
                 val price: Int,
                 val date: Date = Date()) : Parcelable