package org.zapomni.venturers.domain.mapper

import io.reactivex.functions.Function
import org.zapomni.venturers.data.model.response.CuratorResponse
import org.zapomni.venturers.domain.model.Curator
import org.zapomni.venturers.extensions.getPhotoUrlById

class CuratorMapper : Function<CuratorResponse, Curator> {
    override fun apply(response: CuratorResponse): Curator {
        return Curator(response.name ?: "", response.surname ?: "", response.rating?.avgRating
                ?: 0f,
                response.rating?.ratings?.size ?: 0, response.about
                ?: "", response.photo?.getPhotoUrlById(), response.phone)
    }
}