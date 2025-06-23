package com.example.forecast.contract

import com.example.core.domain.models.DailyForecast

data class ForecastWeatherUiState(
    val isLoading: Boolean = false,
    val forecast: List<DailyForecast>? = null,
    val error: String? = null
)

sealed class ForecastWeatherIntent {
    data class LoadForecast(val city: String) : ForecastWeatherIntent()
    object LoadSavedCityForecast : ForecastWeatherIntent()
}