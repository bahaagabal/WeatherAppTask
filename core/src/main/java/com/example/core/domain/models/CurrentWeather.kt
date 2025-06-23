package com.example.core.domain.models

data class CurrentWeather(
    val city: String,
    val temperature: Double,
    val condition: String,
    val iconUrl: String
)
