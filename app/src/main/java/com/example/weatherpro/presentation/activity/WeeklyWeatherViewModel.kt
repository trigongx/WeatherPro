package com.example.weatherpro.presentation.activity

import com.example.weatherpro.core.base.BaseViewModel
import com.example.weatherpro.data.models.HourlyWeatherModel
import com.example.weatherpro.domain.usesaces.GetWeeklyWeatherUseCase
import com.example.weatherpro.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WeeklyWeatherViewModel @Inject constructor(
    private val getWeeklyWeatherUseCase: GetWeeklyWeatherUseCase
): BaseViewModel<HourlyWeatherModel>() {

    private val _weatherData = MutableStateFlow<UiState<HourlyWeatherModel>>(UiState.Empty())
    val weatherData: StateFlow<UiState<HourlyWeatherModel>> get() = _weatherData

    suspend fun getWeeklyWeather(cityName:String) = doOperation(
        operation = { getWeeklyWeatherUseCase.executeRequest(cityName = cityName)}
    )

}