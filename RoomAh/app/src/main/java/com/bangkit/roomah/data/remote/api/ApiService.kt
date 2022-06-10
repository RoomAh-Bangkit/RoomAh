package com.bangkit.roomah.data.remote.api

import com.bangkit.roomah.data.remote.response.ClassifyResponses
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    /**
     * API service function that handle uploading image process
     *
     * @param file Multipart Image
     **/
    @Multipart
    @POST("/classify")
    suspend fun classifyImage(
        @Part file: MultipartBody.Part
    ): ClassifyResponses
}