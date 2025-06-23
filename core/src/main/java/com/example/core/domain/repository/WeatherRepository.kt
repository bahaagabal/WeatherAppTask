package com.example.core.domain.repository

import com.example.core.domain.DataError
import com.example.core.domain.models.CurrentWeather
import com.example.core.domain.models.DailyForecast
import com.example.core.domain.Result

interface WeatherRepository {
    suspend fun getCurrentWeather(city: String): Result<CurrentWeather, DataError.Remote>
    suspend fun get7DayForecast(city: String): Result<List<DailyForecast>, DataError.Remote>
    suspend fun saveLastSearchedCity(city: String)
    suspend fun getLastSearchedCity(): String?
}