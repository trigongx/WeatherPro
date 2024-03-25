package com.example.weatherpro.domain.usesaces

import com.example.weatherpro.domain.repository.RetrofitRepository

class GetWeeklyWeatherUseCase(private val retrofitRepository: RetrofitRepository) {
    suspend fun executeRequest(cityName:String) =
        retrofitRepository.getWeeklyWeather(cityName = cityName)
}