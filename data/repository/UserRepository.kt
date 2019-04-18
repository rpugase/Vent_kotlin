package org.zapomni.venturers.data.repository

import io.reactivex.Single
import org.zapomni.venturers.data.model.BaseResponse
import org.zapomni.venturers.data.model.response.CuratorResponse
import org.zapomni.venturers.data.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserRepository {

    @GET("api/user/me")
    fun getUserSync(): Call<BaseResponse<UserResponse>>

    @POST("api/user/update")
    @FormUrlEncoded
    fun saveUserSync(@Field("name") name: String?,
                     @Field("surname") surname: String?,
                     @Field("photo") photoId: String? = null): Call<BaseResponse<UserResponse>>

    @GET("api/user/curators")
    fun getCurators(): Single<BaseResponse<List<CuratorResponse>>>

    @GET("api/user/{id}")
    fun getUserByIdSync(@Path("id") userId: String): Call<BaseResponse<UserResponse>>
}