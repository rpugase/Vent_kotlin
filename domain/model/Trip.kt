package org.zapomni.venturers.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Trip(val event: Event = Event(),
                val startPoint: TripStartPoint = TripStartPoint(),
                val whatTake: String = "",
                val tripActions: List<TripAction> = listOf(),
                var type: Type = Type.MINI_TRIP) : Parcelable {

    enum class Type {
        TRIP_FIRST, TRIP_SECOND, TRIP_THIRD, MINI_TRIP
    }

    fun isEmpty() = startPoint.isEmpty() && whatTake.isEmpty() && tripActions.isEmpty()
            && startPoint.coordinates == LatLng(.0, .0)

    fun isNotEmpty() = !isEmpty()
}

@Parcelize
data class TripAction(val name: String = "Наш Маршрут",
                      val icon: String = "http://cdn.onlinewebfonts.com/svg/img_243753.png",
                      val images: List<String> = listOf("https://files.adme.ru/files/news/part_130/1303115/10646315-5572493523_9c0c902757_o-1000-e7742b7f0f-1477574309.jpg",
                              "https://files.adme.ru/files/news/part_130/1303115/10646315-5572493523_9c0c902757_o-1000-e7742b7f0f-1477574309.jpg",
                              "https://files.adme.ru/files/news/part_130/1303115/10646315-5572493523_9c0c902757_o-1000-e7742b7f0f-1477574309.jpg",
                              "https://files.adme.ru/files/news/part_130/1303115/10646315-5572493523_9c0c902757_o-1000-e7742b7f0f-1477574309.jpg"),
                      val descriptions: List<String> = listOf("", "Красиво", "", "")) : Parcelable

@Parcelize
data class TripStartPoint(val coordinates: LatLng = LatLng(.0, .0),
                          val address: String = "",
                          val description: String? = null) : Parcelable {
    fun isEmpty() = address.isEmpty() || description.isNullOrEmpty()
}