package com.example.currentweather.model

import com.example.core.domain.models.CurrentWeather

data class CurrentWeatherUiState(
    val weather: CurrentWeather? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

