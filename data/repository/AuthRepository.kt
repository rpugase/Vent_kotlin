package org.zapomni.venturers.data.repository

import io.reactivex.Single
import org.zapomni.venturers.data.model.BaseResponse
import org.zapomni.venturers.data.model.response.AccessTokenResponse
import org.zapomni.venturers.data.model.response.RefreshTokenResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthRepository {

    @POST("api/auth/sms/request")
    @FormUrlEncoded
    fun login(@Field("phone") phone: String): Single<BaseResponse<String>>

    @POST("api/auth/sms/request")
    @FormUrlEncoded
    fun loginSync(@Field("phone") phone: String): Call<BaseResponse<String>>

    @POST("api/auth/sms/submit")
    @FormUrlEncoded
    fun submitLogin(@Field("phone") phone: String, @Field("code") code: String): Single<BaseResponse<RefreshTokenResponse>>

    @POST("api/auth/sms/submit")
    @FormUrlEncoded
    fun submitLoginSync(@Field("phone") phone: String, @Field("code") code: String): Call<BaseResponse<RefreshTokenResponse>>

    @POST("api/auth/access")
    @FormUrlEncoded
    fun getAccessTokenSync(@Field("refreshToken") refreshToken: String): Call<BaseResponse<AccessTokenResponse>>
}