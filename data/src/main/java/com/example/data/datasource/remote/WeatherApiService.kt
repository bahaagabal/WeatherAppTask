package com.example.data.datasource.remote

import com.example.data.dto.CurrentWeatherDto
import com.example.data.dto.ForecastDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<CurrentWeatherDto>

    @GET("forecast/daily")
    suspend fun get7DayForecast(
        @Query("q") city: String,
        @Query("cnt") count: Int = 7,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<ForecastDto>
}