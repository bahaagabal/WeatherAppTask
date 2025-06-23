package com.example.data.datasource

import com.example.core.domain.DataError
import com.example.core.domain.Result
import com.example.data.dto.CurrentWeatherDto
import com.example.data.dto.DailyForecastDto

interface RemoteDataSource {
    suspend fun getCurrentWeather(city: String): Result<CurrentWeatherDto, DataError.Remote>
    suspend fun get7DayForecast(city: String): Result<List<DailyForecastDto>, DataError.Remote>
}