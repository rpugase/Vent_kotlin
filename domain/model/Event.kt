package org.zapomni.venturers.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Event(val id: String = "",
                 val title: String = "",
                 val curator: Curator? = null,
                 val info: String = "",
                 val priceDisclaimer: String = "",
                 val headImages: List<String> = listOf(),
                 val icon: String = "",
                 val content: List<Pair<String, String>> = listOf(),
                 val photos: List<String> = listOf(),
                 val date: InnerDate = InnerDate(),
                 var needShowDate: Boolean = false,
                 val chatId: String? = null) : Parcelable {

    @Parcelize
    class InnerDate(val startDate: Date = Date(),
                    val endDate: Date = Date(),
                    val offers: List<Offer> = listOf()) : Parcelable {

        @Parcelize
        class Offer(val name: String,
                    val currency: Currency,
                    val offerTypes: List<Type>) : Parcelable {
            @Parcelize
            class Type(val type: String,
                       val price: Int,
                       val deposit: Int) : Parcelable
        }
    }
}