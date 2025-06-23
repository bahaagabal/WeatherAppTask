package com.example.core.domain.usecases

import com.example.core.domain.DataError
import com.example.core.domain.Result
import com.example.core.domain.models.DailyForecast
import com.example.core.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Result<List<DailyForecast>, DataError.Remote> {
        return repository.get7DayForecast(city)
    }
}