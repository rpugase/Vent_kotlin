package org.zapomni.venturers.data.repository

import io.reactivex.Single
import org.zapomni.venturers.domain.model.Curator
import retrofit2.http.GET

interface CuratorRepository {

    @GET("api/user/curators")
    fun getCurators(): Single<List<Curator>>
}