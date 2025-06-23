package com.example.currentweather.model

import com.example.core.domain.models.CurrentWeather
import com.example.core.presentation.UiText

data class CurrentWeatherUiState(
    val weather: CurrentWeather? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null
)

