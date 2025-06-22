package com.example.core.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatDayFromTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("EEE", Locale.getDefault())
    return sdf.format(Date(timestamp * 1000))
}