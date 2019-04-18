package org.zapomni.venturers.domain.mapper

import org.zapomni.venturers.data.model.response.*
import org.zapomni.venturers.data.model.response.firebase.ChestFireResponse
import org.zapomni.venturers.data.model.response.firebase.ChestWinnerFireResponse
import org.zapomni.venturers.data.model.response.firebase.MeetingFireResponse
import org.zapomni.venturers.data.model.response.firebase.ThemeResponse
import org.zapomni.venturers.domain.model.*
import org.zapomni.venturers.domain.model.chat.*
import org.zapomni.venturers.extensions.getPhotoUrlById
import java.util.*

fun UserResponse?.toDomainModel(): User? {
    if (this == null) {
        return null
    }

    return User(
            id = id,
            admin = admin,
            phoneNumber = phone,
            name = name,
            surname = surname,
            image = photo?.getPhotoUrlById(),
            instagram = "gridyn") // todo clear test data
}

fun PollResponse?.toDomainModel(): Poll? {
    if (this == null) {
        return null
    }
    val poll = Poll(id = id, question = name, answers = results?.map { PollAnswer(id, it.key, it.value) }
            ?: listOf(), voted = voted, photoId = image)

    val maxAnswer = poll.answers.sumBy { it.count }
    val winner = poll.answers.maxBy { it.count }
    poll.answers.forEach { it.fillData(maxAnswer, winner) }

    return poll
}

fun MeetingFireResponse?.toDomainModel(): Meet? {
    if (this == null) {
        return null
    }

    return Meet(name = name, place = point, time = Date(date), id = id)
}

fun ChestFireResponse?.toDomainModel(): Chest? {
    if (this == null) {
        return null
    }

    return Chest(id = id, price = prize, type = type, winner = winner.toDomainModel())
}

fun ChestResponse?.toDomainModel(): Chest? {
    if (this == null) {
        return null
    }

    return Chest(id = id, price = prize, type = type)
}

fun ThemeResponse?.toDomainModel(): Agenda? {
    if (this == null) return null

    return Agenda(title, subTitle ?: "", img)
}

fun ChestWinnerFireResponse?.toDomainModel(): User? {
    if (this == null) return null

    return User(id = id, image = img.getPhotoUrlById(), name = name)
}

fun CuratorResponse?.toDomainModel(): Curator? {
    if (this == null) return null

    return Curator(name ?: "", surname ?: "", rating?.avgRating ?: 0f,
            rating?.ratings?.size ?: 0, about ?: "", photo?.getPhotoUrlById(), phone)
}

fun EventResponse?.toDomainModel(): Event? {
    if (this == null) return null

    return Event(id, title, curator.toDomainModel(), info, priceDisclaimer, headImg.map { it.getPhotoUrlById() }, icon, content,
            photos.filter { it != null }.map { it!!.getPhotoUrlById() }, Event.InnerDate(Date(date.startDate), Date(date.endDate),
            date.offers.map {
                Event.InnerDate.Offer(it.name, Currency.getInstance(it.currency),
                        it.offerTypes.map { Event.InnerDate.Offer.Type(it.type, it.price, it.deposit) })
            }))
}

fun TripResponse.toDomainModel(): Trip {

    val now = System.currentTimeMillis()

    val tripType = if (type == "MINI") Trip.Type.MINI_TRIP else when {
        now <= startDate -> Trip.Type.TRIP_FIRST
        now in startDate..endDate -> Trip.Type.TRIP_SECOND
        now >= endDate -> Trip.Type.TRIP_THIRD
        else -> Trip.Type.MINI_TRIP
    }

    return Trip(Event(title = title, date = Event.InnerDate(Date(startDate), Date(endDate)), curator = curator.toDomainModel()),
            TripStartPoint(LatLng(startPoint.x, startPoint.y), startPoint.address, startPoint.description),
            takeWithYourself, info.map { TripAction(it.title, it.icon, it.content) },
            type = tripType)
}