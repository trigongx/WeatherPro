package com.example.weatherpro.domain.usesaces

import com.example.weatherpro.domain.repository.RetrofitRepository

class GetCurrentWeatherUseCase(private val retrofitRepository: RetrofitRepository) {
    suspend fun executeRequest(cityName:String) = retrofitRepository.getCurrentWeather(cityName = cityName)
}