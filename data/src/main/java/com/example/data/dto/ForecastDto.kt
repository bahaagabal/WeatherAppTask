package com.example.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ForecastDto(
    @SerializedName("city")
    val city: CityDto?,
    @SerializedName("list")
    val daily: List<DailyForecastDto>?
)

@Keep
data class CityDto(
    @SerializedName("name")
    val name: String?
)

@Keep
data class DailyForecastDto(
    @SerializedName("dt")
    val date: Long?,
    @SerializedName("temp")
    val temp: TempDto?,
    @SerializedName("weather")
    val weather: List<WeatherDescriptionDto>?
)

@Keep
data class TempDto(
    @SerializedName("day")
    val day: Double?
)