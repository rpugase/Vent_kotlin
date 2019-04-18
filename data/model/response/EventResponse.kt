package org.zapomni.venturers.data.model.response

class EventResponse(val id: String,
                    val title: String,
                    val curator: CuratorResponse,
                    val info: String,
                    val priceDisclaimer: String,
                    val headImg: List<String>,
                    val icon: String,
                    val content: List<Pair<String, String>>,
                    val photos: List<String?>,
                    val date: Date,
                    val bookable: Boolean,
                    val urlName: String) {

    inner class Date(val id: String,
                     val startDate: Long,
                     val endDate: Long,
                     val offers: List<Offer>,
                     val freePlacesLeft: Int,
                     val hasFreePlaces: Boolean) {

        inner class Offer(val name: String,
                          val currency: String,
                          val offerTypes: List<Type>) {
            inner class Type(val type: String,
                             val price: Int,
                             val deposit: Int)
        }
    }
}