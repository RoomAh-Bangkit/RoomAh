package com.bangkit.roomah.ui.home

import androidx.lifecycle.ViewModel
import com.bangkit.roomah.data.repository.ClassifyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val classifyRepo: ClassifyRepository,
) : ViewModel() {
    /**
     * Handle image fetching from local
     *
     * @param path Path folder
     */
    suspend fun fetchAllData(
        path: String
    ): Flow<Result<List<String>>> =
        classifyRepo.fetchAllData(path)
}