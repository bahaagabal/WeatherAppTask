package com.example.core.domain.usecases

import com.example.core.domain.DataError
import com.example.core.domain.Result
import com.example.core.domain.repository.WeatherRepository
import javax.inject.Inject

class GetLastSearchedCityUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(): Result<String?, DataError.Local> {
        return repository.getLastSearchedCity()
    }
}