package com.example.data.mappers

import com.example.data.dto.DailyForecastDto
import com.example.data.dto.TempDto
import com.example.data.dto.WeatherDescriptionDto
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test


class DailyForecastMapperTest {

    private lateinit var mapper: DailyForecastMapper

    @Before
    fun setup() {
        mapper = DailyForecastMapper()
    }

    @Test
    fun `map returns correct DailyForecast when all fields are present`() {
        // Given
        val dto = DailyForecastDto(
            date = 1720000000L,
            temp = TempDto(day = 32.0),
            weather = listOf(WeatherDescriptionDto(condition = "Rainy", icon = "09d"))
        )

        // When
        val result = mapper.map(dto)

        // Then
        assertEquals(1720000000L, result.date)
        assertEquals(32.0, result.temperature)
        assertEquals("Rainy", result.condition)
        assertEquals("https://openweathermap.org/img/wn/09d@2x.png", result.iconUrl)
    }

    @Test
    fun `map handles null fields and empty weather list`() {
        // Given
        val dto = DailyForecastDto(
            date = null,
            temp = null,
            weather = emptyList()
        )

        // When
        val result = mapper.map(dto)

        // Then
        assertEquals(0L, result.date)
        assertEquals(0.0, result.temperature)
        assertEquals("", result.condition)
        assertEquals("https://openweathermap.org/img/wn/@2x.png", result.iconUrl)
    }

    @Test
    fun `map handles null weather list`() {
        // Given
        val dto = DailyForecastDto(
            date = 123456789L,
            temp = TempDto(day = 25.0),
            weather = null
        )

        // When
        val result = mapper.map(dto)

        // Then
        assertEquals(123456789L, result.date)
        assertEquals(25.0, result.temperature)
        assertEquals("", result.condition)
        assertEquals("https://openweathermap.org/img/wn/@2x.png", result.iconUrl)
    }
}
