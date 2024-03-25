package com.example.weatherpro.presentation.activity

import com.example.weatherpro.core.base.BaseViewModel
import com.example.weatherpro.data.models.HourlyWeatherModel
import com.example.weatherpro.domain.usesaces.GetHourlyWeatherUseCase
import com.example.weatherpro.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HourlyWeatherViewModel @Inject constructor(
    private val getHourlyWeatherUseCase: GetHourlyWeatherUseCase
) :BaseViewModel<HourlyWeatherModel>() {

    private val _hourlyWeatherData = MutableStateFlow<UiState<HourlyWeatherModel>>(UiState.Empty())
    val hourlyWeatherData: StateFlow<UiState<HourlyWeatherModel>> get() = _hourlyWeatherData

    suspend fun getHourlyWeather(cityName: String) = doOperation(
        operation = { getHourlyWeatherUseCase.executeRequest(cityName = cityName)}
    )
}