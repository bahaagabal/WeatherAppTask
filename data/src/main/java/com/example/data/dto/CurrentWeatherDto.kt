package com.example.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CurrentWeatherDto(
    @SerializedName("name")
    val cityName: String?,
    @SerializedName("main")
    val main: MainDto?,
    @SerializedName("weather")
    val weather: List<WeatherDescriptionDto>?
)

@Keep
data class MainDto(
    @SerializedName("temp")
    val temperature: Double?
)

@Keep
data class WeatherDescriptionDto(
    @SerializedName("main")
    val condition: String?,
    @SerializedName("icon")
    val icon: String?
)