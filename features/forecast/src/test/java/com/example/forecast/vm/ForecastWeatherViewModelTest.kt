package com.example.forecast.vm

import app.cash.turbine.test
import com.example.core.domain.DataError
import com.example.core.domain.Result
import com.example.core.domain.models.DailyForecast
import com.example.core.domain.usecases.GetForecastUseCase
import com.example.core.domain.usecases.GetLastSearchedCityUseCase
import com.example.core.presentation.UiText
import com.example.core.presentation.toUiText
import com.example.forecast.contract.ForecastWeatherIntent
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastWeatherViewModelTest {

    private val getForecastUseCase: GetForecastUseCase = mockk()
    private val getLastSearchedCityUseCase: GetLastSearchedCityUseCase = mockk()

    private lateinit var viewModel: ForecastWeatherViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ForecastWeatherViewModel(
            getForecastUseCase,
            getLastSearchedCityUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load forecast should emit loading and success state`() = runTest {
        // Arrange
        val forecast = listOf(
            DailyForecast(
                date = 1234567,
                temperature = 25.0,
                condition = "Clear",
                iconUrl = "icon"
            )
        )

        coEvery { getForecastUseCase("Cairo") } returns Result.Success(forecast)

        // Act
        viewModel.onIntent(ForecastWeatherIntent.LoadForecast("Cairo"))
        advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assert(state.isLoading.not())
        assert(state.forecast == forecast)
    }

    @Test
    fun `load forecast should emit loading and error state`() = runTest {
        // Arrange
        val error = DataError.Remote.SERVER
        coEvery { getForecastUseCase("Cairo") } returns Result.Error(error)

        // Act
        viewModel.onIntent(ForecastWeatherIntent.LoadForecast("Cairo"))
        advanceUntilIdle()

        // Assert
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)

        val actual = state.error as UiText.StringResourceId
        val expected = error.toUiText() as UiText.StringResourceId

        assertEquals(expected.id, actual.id)
    }

    @Test
    fun `load saved city forecast should trigger forecast use case if city is found`() = runTest {
        coEvery { getLastSearchedCityUseCase() } returns Result.Success("London")
        coEvery { getForecastUseCase("London") } returns Result.Success(emptyList())

        viewModel.onIntent(ForecastWeatherIntent.LoadSavedCityForecast)

        advanceUntilIdle()

        assert(viewModel.state.value.forecast != null)
    }
}
