package com.example.data.mappers

import com.example.core.domain.models.DailyForecast
import com.example.data.dto.DailyForecastDto
import javax.inject.Inject

class DailyForecastMapper @Inject constructor() {

    fun map(dailyForecastDto: DailyForecastDto): DailyForecast {
        with(dailyForecastDto) {
            val condition = weather?.firstOrNull()?.condition.orEmpty()
            val icon = weather?.firstOrNull()?.icon.orEmpty()
            val iconUrl = "https://openweathermap.org/img/wn/${icon}@2x.png"

            return DailyForecast(
                date = date ?: 0,
                temperature = temp?.day ?: 0.0,
                condition = condition,
                iconUrl = iconUrl
            )
        }
    }
}