package org.zapomni.venturers.data.model.response.firebase

data class ChestFireResponse(val id: String = "",
                             val prize: Int = 0,
                             val type: String = "",
                             val winner: ChestWinnerFireResponse? = null)