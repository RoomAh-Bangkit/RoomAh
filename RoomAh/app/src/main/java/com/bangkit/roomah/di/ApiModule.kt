package com.bangkit.roomah.di

import com.bangkit.roomah.data.remote.api.ApiConfig
import com.bangkit.roomah.data.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    /**
     * Provide API Service instance for Hilt
     *
     * @return ApiService
     **/
    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfig.getApiService()
}