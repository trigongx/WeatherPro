package com.example.weatherpro.core.di

import com.example.weatherpro.domain.repository.RetrofitRepository
import com.example.weatherpro.domain.usesaces.GetCurrentWeatherUseCase
import com.example.weatherpro.domain.usesaces.GetHourlyWeatherUseCase
import com.example.weatherpro.domain.usesaces.GetWeeklyWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    fun provideGetCurrentWeatherUseCase(retrofitRepository: RetrofitRepository) =
        GetCurrentWeatherUseCase(retrofitRepository)

    @Provides
    fun provideGetHourlyWeatherUseCase(retrofitRepository: RetrofitRepository) =
        GetHourlyWeatherUseCase(retrofitRepository)

    @Provides
    fun provideGetWeeklyWeatherUseCase(retrofitRepository: RetrofitRepository) =
        GetWeeklyWeatherUseCase(retrofitRepository)
}