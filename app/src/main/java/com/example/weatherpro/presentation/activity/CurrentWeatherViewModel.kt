package com.example.weatherpro.presentation.activity

import com.example.weatherpro.core.base.BaseViewModel
import com.example.weatherpro.data.models.WeatherModel
import com.example.weatherpro.domain.usesaces.GetCurrentWeatherUseCase
import com.example.weatherpro.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
): BaseViewModel<WeatherModel>() {

    private val _weatherData = MutableStateFlow<UiState<WeatherModel>>(UiState.Empty())
    val weatherData: StateFlow<UiState<WeatherModel>> get() = _weatherData

    suspend fun getCurrentWeather(cityName:String) = doOperation(
        operation = { getCurrentWeatherUseCase.executeRequest(cityName = cityName)}
    )

}