package org.zapomni.venturers.domain

import io.reactivex.Single
import org.zapomni.venturers.data.repository.EventRepository
import org.zapomni.venturers.data.repository.PreferencesRepository
import org.zapomni.venturers.data.repository.TripRepository
import org.zapomni.venturers.domain.mapper.toDomainModel
import org.zapomni.venturers.domain.model.*
import org.zapomni.venturers.domain.model.navigation.TripNavigation

class TripInteractor(private val eventRepository: EventRepository,
                     private val preferencesRepository: PreferencesRepository,
                     private val tripRepository: TripRepository) {

    var tripNavigation = TripNavigation.THIRD
    val tripFeedbackNavigation = preferencesRepository.tripFeedbackNavigation

    private var trips = mutableListOf<Trip>()

    fun getTrip(): Single<Trip> {
        return getTrips().map {
            val mainTrip = it.find { it.type != Trip.Type.MINI_TRIP }
            return@map if (mainTrip != null) mainTrip
            else it.filter { it.type == Trip.Type.MINI_TRIP }.firstOrNull() ?: Trip()
        }
    }

    fun getTrips(): Single<List<Trip>> {
        return getEventsTrips()
    }
//    fun getTrips(): Single<List<Trip>> {
//        return if (trips.isNotEmpty()) Single.just(trips)
//        else tripRepository.getTrips()
//                .map {
//                    it.data?.sortedBy(TripResponse::startDate)?.map(TripResponse::toDomainModel)
//                            ?: listOf()
//                }
//                .map { trips.addAll(it); it }
//    }

    fun finishFeedback() {
        trips.clear()
        preferencesRepository.clearFeedback()
    }

    private fun getEventsTrips(): Single<List<Trip>> {
        return if (trips.isNotEmpty()) Single.just(trips) else eventRepository.getEvents(0)
                .map {
                    if (it.isSuccessful) it.data!!
                            .take(1)
                            .map { it.toDomainModel()!!.toTrip() }
                            .sortedBy { it.event.date.startDate }
                            .also { trips.addAll(it) }
                    else listOf()
                }
    }

    private fun Trip.Type.toTripNavigation() = when (this) {
        Trip.Type.TRIP_FIRST -> TripNavigation.FIRST
        Trip.Type.TRIP_SECOND -> TripNavigation.SECOND
        Trip.Type.TRIP_THIRD -> TripNavigation.THIRD
        Trip.Type.MINI_TRIP -> TripNavigation.MINI_TRIP
    }

    private fun Event.toTrip() = Trip(this, TripStartPoint(LatLng(46.467755, 30.740744),
            "Привокзальна площа, 2, Одеса, Одеська область, Украина, 65000", "Встречаемся в 09:00 на главном вокзале Одессы"),
            "Вот что вам может пригодиться:\n" +
                    "— Сигареты\n" +
                    "— Алкоголь\n" +
                    "— Мобильный телефон\n" +
                    "— Лыжи\n" +
                    "— Палки\n" +
                    "— Деньги\n" +
                    "— Хорошее настроение!",
            listOf(TripAction(), TripAction(), TripAction(), TripAction(), TripAction()),
            Trip.Type.TRIP_FIRST)

}