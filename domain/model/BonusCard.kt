package org.zapomni.venturers.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BonusCard(val cardType: CardType,
                     val cash: Int,
                     val bonuses: List<Bonus>) : Parcelable