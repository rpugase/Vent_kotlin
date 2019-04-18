package org.zapomni.venturers.domain.model

data class TripFeedbackCollector(val eventId: String = "",
                                 val ratingEvent: Int = 0,
                                 val ratingCurator: Int = 0,
                                 val review: String = "",
                                 val prizeId: String = "")