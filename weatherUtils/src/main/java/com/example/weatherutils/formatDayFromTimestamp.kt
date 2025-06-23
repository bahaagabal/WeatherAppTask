package com.example.weatherutils

import java.text.SimpleDateFormat
import java.util.*

fun formatDayFromTimestamp(timestamp: Long, locale: Locale = Locale.ENGLISH): String {
    val sdf = SimpleDateFormat("EEE", locale)
    return sdf.format(Date(timestamp * 1000))
}