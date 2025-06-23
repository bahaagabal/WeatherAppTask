package com.example.forecast.vm

import com.example.core.domain.DataError
import com.example.core.domain.Result
import com.example.core.domain.models.DailyForecast
import com.example.core.domain.usecases.GetForecastUseCase
import com.example.core.domain.usecases.GetLastSearchedCityUseCase
import com.example.forecast.contract.ForecastWeatherIntent
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ForecastWeatherViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ForecastWeatherViewModel
    private val getForecastUseCase = mockk<GetForecastUseCase>()
    private val getLastSearchedCityUseCase = mockk<GetLastSearchedCityUseCase>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ForecastWeatherViewModel(getForecastUseCase, getLastSearchedCityUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `LoadForecast intent updates uiState with success`() = runTest {
        val city = "Luxor"
        val forecastList = listOf(
            DailyForecast(date = 1234L, temperature = 33.0, condition = "Clear", iconUrl = "icon1"),
            DailyForecast(date = 5678L, temperature = 29.0, condition = "Clouds", iconUrl = "icon2")
        )

        coEvery { getForecastUseCase(city) } returns Result.Success(forecastList)

        viewModel.onIntent(ForecastWeatherIntent.LoadForecast(city))
        testDispatcher.scheduler.advanceUntilIdle()

        with(viewModel.uiState) {
            assertFalse(isLoading)
            assertEquals(forecastList, forecast)
            assertEquals(null, error)
        }

        coVerify { getForecastUseCase(city) }
    }

    @Test
    fun `LoadForecast intent updates uiState with error`() = runTest {
        val city = "Aswan"
        val error = Result.Error(DataError.Remote.SERVER)

        coEvery { getForecastUseCase(city) } returns error

        viewModel.onIntent(ForecastWeatherIntent.LoadForecast(city))
        testDispatcher.scheduler.advanceUntilIdle()

        with(viewModel.uiState) {
            assertFalse(isLoading)

            assertTrue(error.error.name.contains(DataError.Remote.SERVER.toString()))
            assertEquals(null, forecast)
        }

        coVerify { getForecastUseCase(city) }
    }

    @Test
    fun `LoadSavedCityForecast intent loads and fetches forecast`() = runTest {
        val savedCity = "Cairo"
        val forecastList = listOf(DailyForecast(0L, 30.0, "Sunny", "icon"))

        coEvery { getLastSearchedCityUseCase() } returns Result.Success(savedCity)
        coEvery { getForecastUseCase(savedCity) } returns Result.Success(forecastList)

        viewModel.onIntent(ForecastWeatherIntent.LoadSavedCityForecast)
        testDispatcher.scheduler.advanceUntilIdle()

        with(viewModel.uiState) {
            assertFalse(isLoading)
            assertEquals(forecastList, forecast)
        }

        coVerify { getLastSearchedCityUseCase() }
        coVerify { getForecastUseCase(savedCity) }
    }

    @Test
    fun `LoadSavedCityForecast does nothing when saved city is blank`() = runTest {
        coEvery { getLastSearchedCityUseCase() } returns Result.Success("")

        viewModel.onIntent(ForecastWeatherIntent.LoadSavedCityForecast)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { getForecastUseCase(any()) }
    }
}
