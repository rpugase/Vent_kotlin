package org.zapomni.venturers.data.model.response

data class CuratorResponse(val id: String?,
                           val visible: Boolean?,
                           val mail: String?,
                           val name: String?,
                           val surname: String?,
                           val photo: String?,
                           val phone: String?,
                           val about: String?,
                           val curator: Boolean?,
                           val admin: Boolean?,
                           val rating: Rating?) {

    inner class Rating(val id: String?,
                       val parentId: String?,
                       val ratings: Map<String, Int>?,
                       val avgRating: Float?)
}