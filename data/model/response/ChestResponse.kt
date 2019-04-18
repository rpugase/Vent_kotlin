package org.zapomni.venturers.data.model.response

data class ChestResponse(val id: String,
                         val prize: Int,
                         val winnerId: String,
                         val parentMessage: String,
                         val type: String)