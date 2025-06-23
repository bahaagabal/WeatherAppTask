package com.example.core.domain.models

data class DailyForecast(
    val date: Long,
    val temperature: Double,
    val condition: String,
    val iconUrl: String
)
