package com.example.core.domain.usecases

import com.example.core.domain.DataError
import com.example.core.domain.models.CurrentWeather
import com.example.core.domain.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test
import com.example.core.domain.Result
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class GetCurrentWeatherUseCaseTest {

    private val repository: WeatherRepository = mockk()
    private lateinit var useCase: GetCurrentWeatherUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        useCase = GetCurrentWeatherUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke returns Success when repository returns success`() = runTest {
        // Given
        val city = "Riyadh"
        val fakeWeather = CurrentWeather(
            city = "xyz" ,
            temperature = 25.0 ,
            condition = "Sunny",
            iconUrl = "https://www.google.com"
        )

        coEvery { repository.getCurrentWeather(city) } returns Result.Success(fakeWeather)

        // When
        val result = useCase(city)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(fakeWeather, (result as Result.Success).data)
    }

    @Test
    fun `invoke returns Error when repository returns error`() = runTest {
        // Given
        val city = "Cairo"
        val error = DataError.Remote.REQUEST_TIMEOUT

        coEvery { repository.getCurrentWeather(city) } returns Result.Error(error)

        // When
        val result = useCase(city)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).error)
    }
}

