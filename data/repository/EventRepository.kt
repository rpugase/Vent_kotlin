package org.zapomni.venturers.data.repository

import io.reactivex.Single
import org.zapomni.venturers.data.model.BasePaginationResponse
import org.zapomni.venturers.data.model.BaseResponse
import org.zapomni.venturers.data.model.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventRepository {

    @GET("list")
    fun getEvents(@Query("page") page: Int): Single<BasePaginationResponse<List<EventResponse>>>

    @GET("list")
    fun getEventsSync(@Query("page") page: Int): Call<BasePaginationResponse<List<EventResponse>>>

    @GET("closest")
    fun getImportantEvents(@Query("count") count: Int = 3): Single<BaseResponse<List<EventResponse>>>

    @GET("closest")
    fun getImportantEventsSync(@Query("count") count: Int = 3): Call<BaseResponse<List<EventResponse>>>

    @GET("{id}")
    fun getEvent(@Path("id") id: String): Single<BaseResponse<EventResponse>>
}