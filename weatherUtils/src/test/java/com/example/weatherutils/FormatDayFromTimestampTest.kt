package com.example.weatherutils

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatDayFromTimestampTest {

    @Test
    fun `formatDayFromTimestamp returns correct day abbreviation`() {
        val timestamp = 1563897600L

        val result = formatDayFromTimestamp(timestamp)
        assertEquals("Tue", result)
    }
}