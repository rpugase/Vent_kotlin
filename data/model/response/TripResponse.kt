package org.zapomni.venturers.data.model.response

class TripResponse(val id: String,
                   val title: String,
                   val curator: CuratorResponse,
                   val startDate: Long,
                   val endDate: Long,
                   val startPoint: StartPoint,
                   val visible: Boolean,
                   val type: String, // FULL, MINI
                   val chatId: String,
                   val info: List<Info>,
                   val takeWithYourself: String) {

    inner class Info(val title: String,
                     val icon: String,
                     val content: List<String>)

    inner class StartPoint(val x: Double,
                           val y: Double,
                           val address: String,
                           val description: String?)
}