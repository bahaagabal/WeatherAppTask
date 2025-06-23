package com.example.core.domain.usecases

import com.example.core.domain.models.DailyForecast
import com.example.core.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.example.core.domain.Result
import com.example.core.domain.DataError
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue


class GetForecastUseCaseTest {

    private val repository = mockk<WeatherRepository>()
    private lateinit var useCase: GetForecastUseCase

    @Before
    fun setup() {
        useCase = GetForecastUseCase(repository)
    }

    @Test
    fun `invoke returns Success when repository returns forecast data`() = runTest {
        // Given
        val city = "Riyadh"
        val forecastList = listOf(
            DailyForecast(
                date = 1687507200000L,
                temperature = 30.0,
                condition = "Sunny",
                iconUrl = "01d"
            ),
            DailyForecast(
                date = 1687507200000L,
                temperature = 28.0,
                condition = "Cloudy",
                iconUrl = "02d"
            )
        )

        coEvery { repository.get7DayForecast(city) } returns Result.Success(forecastList)

        // When
        val result = useCase(city)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(forecastList, (result as Result.Success).data)
    }

    @Test
    fun `invoke returns Error when repository returns error`() = runTest {
        // Given
        val city = "Cairo"
        val error = DataError.Remote.SERVER

        coEvery { repository.get7DayForecast(city) } returns Result.Error(error)

        // When
        val result = useCase(city)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).error)
    }
}
