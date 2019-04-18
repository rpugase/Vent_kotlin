package org.zapomni.venturers.domain.model.chat

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.zapomni.venturers.domain.model.User

@Parcelize
data class Chest(val id: String,
                 val price: Int,
                 val type: String,
                 val winner: User? = null) : Parcelable {

    object Type {
        const val SIMPLE = "Bronze"
        const val GOLD = "Gold"
    }
}