package com.example.forecast.contract

import com.example.core.domain.models.DailyForecast
import com.example.core.presentation.UiText

data class ForecastWeatherUiState(
    val isLoading: Boolean = false,
    val forecast: List<DailyForecast>? = null,
    val error: UiText? = null
)

sealed class ForecastWeatherIntent {
    data class LoadForecast(val city: String) : ForecastWeatherIntent()
    data object LoadSavedCityForecast : ForecastWeatherIntent()
}