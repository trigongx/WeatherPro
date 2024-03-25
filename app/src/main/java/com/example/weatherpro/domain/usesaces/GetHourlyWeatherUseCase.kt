package com.example.weatherpro.domain.usesaces

import com.example.weatherpro.domain.repository.RetrofitRepository

class GetHourlyWeatherUseCase(private val retrofitRepository: RetrofitRepository) {
    suspend fun executeRequest(cityName:String) =
        retrofitRepository.getHourlyWeather(cityName = cityName)
}