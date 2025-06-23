package com.example.core.domain.usecases

import com.example.core.domain.DataError
import com.example.core.domain.Result
import com.example.core.domain.repository.WeatherRepository
import javax.inject.Inject

class SaveLastSearchedCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Result<Unit, DataError.Local> {
        return repository.saveLastSearchedCity(city)
    }
}