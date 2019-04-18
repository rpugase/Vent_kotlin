package org.zapomni.venturers.data.repository

import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Single
import org.zapomni.venturers.data.model.BaseResponse
import org.zapomni.venturers.data.model.response.ChatResponse
import org.zapomni.venturers.data.model.response.ChestResponse
import org.zapomni.venturers.data.model.response.PollResponse
import retrofit2.Call
import retrofit2.http.*

interface ChatRepository {

    @POST("send/{chatId}")
    @FormUrlEncoded
    fun sendMessageSync(
            @Path("chatId") chatId: String,
            @Field("message") message: String,
            @Field("onTopic") onTopic: Boolean,
            @Field("photo") photo: String? = null,
            @Field("reply") replyMessageId: String? = null
    ): Call<BaseResponse<JsonObject>>

    @POST("util/sendMeeting")
    @FormUrlEncoded
    fun sendMeetingSync(
            @Field("chatId") chatId: String,
            @Field("name") name: String,
            @Field("meetingPoint") meetingPoint: String,
            @Field("time") time: Long,
            @Field("onTopic") onTopic: Boolean
    ): Call<BaseResponse<JsonObject>>

    @POST("like")
    @FormUrlEncoded
    fun makeLike(@Field("chatId") chatId: String,
                 @Field("messageId") messageId: String,
                 @Field("status") status: Boolean): Single<BaseResponse<String>>

    @GET("util/fulldata/all")
    fun getFullChats(): Single<BaseResponse<List<ChatResponse>>>

    @GET("util/fulldata/all")
    fun getFullChatsSync(): Call<BaseResponse<List<ChatResponse>>>

    @POST("poll/create")
    @FormUrlEncoded
    fun createPoll(@Field("chatId") chatId: String,
                   @Field("name") name: String,
                   @Field("options") options: Array<String>,
                   @Field("image") image: String?): Call<BaseResponse<PollResponse>>

    @GET("poll/{id}")
    fun getPoll(@Query("id") id: String): Single<BaseResponse<String>>

    @POST("poll/vote")
    @FormUrlEncoded
    fun votePollSync(@Field("pollId") pollId: String,
                     @Field("option") option: String): Call<BaseResponse<JsonObject>>

    @POST("chest/grab/{chestId}")
    fun grabChestSync(@Path("chestId") chestId: String): Call<BaseResponse<ChestResponse>>

    @POST("theme")
    @FormUrlEncoded
    fun createAgendaSync(@Field("chatId") chatId: String,
                         @Field("title") title: String,
                         @Field("image") image: String? = null,
                         @Field("subtitle") subtitle: String? = null,
                         @Field("pollId") pollId: String? = null): Call<BaseResponse<JsonObject>>

    @POST("remove/delete/{chatId}")
    @FormUrlEncoded
    fun deleteMessage(@Path("chatId") chatId: String,
                      @Field("messageId") messageId: String): Completable

    @POST("remove/ban/temp")
    @FormUrlEncoded
    fun banTemp(@Field("chatId") chatId: String,
                @Field("userId") userId: String,
                @Field("hours") hours: Int = 24): Completable

    @POST("remove/ban/permanent")
    @FormUrlEncoded
    fun banPermanent(@Field("chatId") chatId: String,
                     @Field("userId") userId: String): Completable
}