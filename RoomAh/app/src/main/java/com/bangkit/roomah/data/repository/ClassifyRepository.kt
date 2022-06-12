package com.bangkit.roomah.data.repository

import com.bangkit.roomah.data.remote.api.ApiService
import com.bangkit.roomah.data.remote.response.ClassifyResponses
import okhttp3.MultipartBody
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class ClassifyRepository @Inject constructor(
    private val apiService: ApiService
) {
    /**
     * Handle image uploading process to the server
     *
     * @param file Image file
     */
    suspend fun classifyImage(
        file: MultipartBody.Part
    ): Flow<Result<ClassifyResponses>> = flow {
        try {
            val response = apiService.classifyImage(file)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    /**
     * Handle image fetching from local
     *
     * @param path Path folder
     */
    suspend fun fetchAllData(
        path: String
    ): Flow<Result<List<String>>> = flow {
        try {
            val response = mutableListOf<String>()
            File(path).listFiles()?.forEach {
                response.add(it.path)
            }
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }


}