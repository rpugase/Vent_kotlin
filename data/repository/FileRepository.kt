package org.zapomni.venturers.data.repository

import okhttp3.MultipartBody
import org.zapomni.venturers.data.model.BaseResponse
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileRepository {

    @Multipart
    @POST("api/file/upload")
    fun uploadFileSync(@Part file: MultipartBody.Part): Call<BaseResponse<String>>
}