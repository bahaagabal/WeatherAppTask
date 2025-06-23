package com.example.core.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyForecast(
    val date: Long,
    val temperature: Double,
    val condition: String,
    val iconUrl: String
) : Parcelable
