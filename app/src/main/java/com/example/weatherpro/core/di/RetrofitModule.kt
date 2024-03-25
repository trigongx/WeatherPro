package com.example.weatherpro.core.di

import com.example.weatherpro.data.remote.network.ApiService
import com.example.weatherpro.data.remote.network.RetrofitClient
import com.example.weatherpro.data.repository.RetrofitRepositoryImpl
import com.example.weatherpro.domain.repository.RetrofitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofitClient() = RetrofitClient()

    @Provides
    @Singleton
    fun provideApiService(retrofitClient: RetrofitClient) = retrofitClient.createApiService()

    @Provides
    @Singleton
    fun provideRepository(apiService: ApiService):RetrofitRepository = RetrofitRepositoryImpl(apiService)


}