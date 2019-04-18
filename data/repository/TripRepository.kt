package org.zapomni.venturers.data.repository

import io.reactivex.Single
import org.zapomni.venturers.data.model.BaseResponse
import org.zapomni.venturers.data.model.response.TripResponse
import retrofit2.http.GET

interface TripRepository {

    @GET("list")
    fun getTrips(): Single<BaseResponse<List<TripResponse>>>
}