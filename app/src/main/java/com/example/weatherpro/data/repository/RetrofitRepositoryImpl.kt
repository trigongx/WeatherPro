package com.example.weatherpro.data.repository

import com.example.weatherpro.data.models.WeatherModel
import com.example.weatherpro.data.models.HourlyWeatherModel
import com.example.weatherpro.data.remote.network.ApiService
import com.example.weatherpro.domain.repository.RetrofitRepository
import com.example.weatherpro.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RetrofitRepositoryImpl (private val apiService: ApiService): RetrofitRepository {

    override suspend fun getCurrentWeather(cityName:String): Flow<UiState<WeatherModel>>{
        return flow {
            emit(UiState.Loading())
            try {
                val data = apiService.getCurrentWeather(cityName = cityName)
                emit(UiState.Success(data))
            } catch (e:Exception){
                emit(UiState.Error(e.localizedMessage))
            }
        }
    }

    override suspend fun getHourlyWeather(cityName: String): Flow<UiState<HourlyWeatherModel>> {
        return flow {
            emit(UiState.Loading())
            try {
                val data = apiService.getHourlyWeather(cityName = cityName)
                emit(UiState.Success(data))
            } catch (e:Exception){
                emit(UiState.Error(e.localizedMessage))
            }
        }
    }

    override suspend fun getWeeklyWeather(cityName: String): Flow<UiState<HourlyWeatherModel>> {
        return flow {
            emit(UiState.Loading())
            try {
                val data = apiService.getWeeklyWeather(cityName = cityName)
                emit(UiState.Success(data))
            } catch (e:Exception){
                emit(UiState.Error(e.localizedMessage))
            }
        }
    }


}