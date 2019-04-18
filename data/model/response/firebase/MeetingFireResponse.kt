package org.zapomni.venturers.data.model.response.firebase

data class MeetingFireResponse(val id: String = "",
                               val name: String = "",
                               val point: String = "",
                               val date: Long = System.currentTimeMillis())