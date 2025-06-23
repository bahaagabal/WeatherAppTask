package com.example.data.mappers

import com.example.core.domain.models.CurrentWeather
import com.example.data.dto.CurrentWeatherDto
import javax.inject.Inject

class CurrentWeatherMapper @Inject constructor() {

    fun map(currentWeatherDto: CurrentWeatherDto): CurrentWeather {

        with(currentWeatherDto) {
            val condition = weather?.firstOrNull()?.condition.orEmpty()
            val icon = weather?.firstOrNull()?.icon.orEmpty()
            val iconUrl = "https://openweathermap.org/img/wn/${icon}@2x.png"

            return CurrentWeather(
                city = cityName.orEmpty(),
                temperature = main?.temperature ?: 0.0,
                condition = condition,
                iconUrl = iconUrl
            )
        }

    }
}