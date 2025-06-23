package com.example.core.domain.usecases

import com.example.core.domain.DataError
import com.example.core.domain.Result
import com.example.core.domain.models.CurrentWeather
import com.example.core.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Result<CurrentWeather, DataError.Remote> {
        return repository.getCurrentWeather(city)
    }
}