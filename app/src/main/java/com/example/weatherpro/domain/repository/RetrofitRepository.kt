package com.example.weatherpro.domain.repository

import com.example.weatherpro.data.models.WeatherModel
import com.example.weatherpro.data.models.HourlyWeatherModel
import com.example.weatherpro.utils.UiState
import kotlinx.coroutines.flow.Flow

interface RetrofitRepository {
    suspend fun getCurrentWeather(cityName:String): Flow<UiState<WeatherModel>>
    suspend fun getHourlyWeather(cityName: String): Flow<UiState<HourlyWeatherModel>>
    suspend fun getWeeklyWeather(cityName: String): Flow<UiState<HourlyWeatherModel>>
}