package com.example.weatherpro.data.remote.network

import com.example.weatherpro.data.models.WeatherModel
import com.example.weatherpro.data.models.HourlyWeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName:String = "Bishkek",
        @Query("units") units: String = "metric",
        @Query("appid") appid:String = "36cbf21e77603d1848c9293fd5e5ba31"
    ):WeatherModel

    @GET("data/2.5/forecast")
    suspend fun getHourlyWeather(
        @Query("q") cityName: String = "Bishkek",
        @Query("cnt") periodCount:Int = 5,
        @Query("units") units: String = "metric",
        @Query("appid") appid:String = "36cbf21e77603d1848c9293fd5e5ba31"
    ):HourlyWeatherModel

    @GET("data/2.5/forecast")
    suspend fun getWeeklyWeather(
        @Query("q") cityName: String = "Bishkek",
        @Query("units") units: String = "metric",
        @Query("appid") appid:String = "36cbf21e77603d1848c9293fd5e5ba31"
    ):HourlyWeatherModel
}