package org.zapomni.venturers.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val phoneNumber: String? = null,
                var name: String? = null,
                var surname: String? = null,
                var image: String? = null,
                var bonusCard: BonusCard = BonusCard(CardType.NONE, 0, listOf()),
                val events: MutableList<Event> = mutableListOf(),
                val id: String? = null,
                val admin: Boolean = false,
                val instagram: String? = null) : Parcelable {

    fun addEvent() { // todo fix logic
        events.add(Event())
        val countEvents = events.size

        bonusCard = when {
            countEvents < 5 -> BonusCard(CardType.GOLD, 0, listOf())
            countEvents < 25 -> BonusCard(CardType.GREEN, 0, listOf())
            countEvents < 50 -> BonusCard(CardType.BLUE, 0, listOf())
            else -> BonusCard(CardType.NONE, 0, listOf())
        }
    }
}