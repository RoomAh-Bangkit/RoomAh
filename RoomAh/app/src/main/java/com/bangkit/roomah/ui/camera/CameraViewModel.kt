package com.bangkit.roomah.ui.camera

import androidx.lifecycle.ViewModel
import com.bangkit.roomah.data.remote.response.ClassifyResponses
import com.bangkit.roomah.data.repository.ClassifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class  CameraViewModel @Inject constructor(
    private val classifyRepo: ClassifyRepository,
) : ViewModel() {
    /**
     * Handle image uploading process to the server
     *
     * @param file Image file
     **/
    suspend fun classifyImage(
        file: MultipartBody.Part
    ): Flow<Result<ClassifyResponses>> =
        classifyRepo.classifyImage(file)
}