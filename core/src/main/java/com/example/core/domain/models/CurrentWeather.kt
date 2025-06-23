package com.example.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentWeather(
    val city: String,
    val temperature: Double,
    val condition: String,
    val iconUrl: String
) : Parcelable
