package com.example.data.mappers

import com.example.data.dto.CurrentWeatherDto
import com.example.data.dto.MainDto
import com.example.data.dto.WeatherDescriptionDto
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test


class CurrentWeatherMapperTest {

    private lateinit var mapper: CurrentWeatherMapper

    @Before
    fun setup() {
        mapper = CurrentWeatherMapper()
    }

    @Test
    fun `map returns correct CurrentWeather when all fields are present`() {
        // Given
        val dto = CurrentWeatherDto(
            cityName = "Riyadh",
            main = MainDto(temperature = 36.5),
            weather = listOf(WeatherDescriptionDto(condition = "Clear", icon = "01d"))
        )

        // When
        val result = mapper.map(dto)

        // Then
        assertEquals("Riyadh", result.city)
        assertEquals(36.5, result.temperature)
        assertEquals("Clear", result.condition)
        assertEquals("https://openweathermap.org/img/wn/01d@2x.png", result.iconUrl)
    }

    @Test
    fun `map handles nulls and empty lists correctly`() {
        // Given
        val dto = CurrentWeatherDto(
            cityName = null,
            main = null,
            weather = emptyList()
        )

        // When
        val result = mapper.map(dto)

        // Then
        assertEquals("", result.city)
        assertEquals(0.0, result.temperature)
        assertEquals("", result.condition)
        assertEquals("https://openweathermap.org/img/wn/@2x.png", result.iconUrl)
    }

    @Test
    fun `map handles null weather list`() {
        // Given
        val dto = CurrentWeatherDto(
            cityName = "Jeddah",
            main = MainDto(temperature = 40.0),
            weather = null
        )

        // When
        val result = mapper.map(dto)

        // Then
        assertEquals("Jeddah", result.city)
        assertEquals(40.0, result.temperature)
        assertEquals("", result.condition)
        assertEquals("https://openweathermap.org/img/wn/@2x.png", result.iconUrl)
    }
}
