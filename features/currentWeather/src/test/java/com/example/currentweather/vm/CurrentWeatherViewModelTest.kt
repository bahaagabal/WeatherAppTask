package com.example.currentweather.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.core.domain.DataError
import com.example.core.domain.Result
import com.example.core.domain.models.CurrentWeather
import com.example.core.domain.usecases.GetCurrentWeatherUseCase
import com.example.core.domain.usecases.GetLastSearchedCityUseCase
import com.example.core.presentation.UiText
import com.example.currentweather.model.CurrentWeatherUiState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrentWeatherViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CurrentWeatherViewModel
    private val getCurrentWeatherUseCase = mockk<GetCurrentWeatherUseCase>()
    private val getLastSearchedCityUseCase = mockk<GetLastSearchedCityUseCase>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CurrentWeatherViewModel(getCurrentWeatherUseCase, getLastSearchedCityUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadSavedCityWeather loads weather when saved city is not blank`() = runTest {
        val city = "Cairo"
        val mockWeather =
            CurrentWeather(city = city, temperature = 30.0, condition = "Clear", iconUrl = "")

        coEvery { getLastSearchedCityUseCase() } returns Result.Success(city)
        coEvery { getCurrentWeatherUseCase(city) } returns Result.Success(mockWeather)

        val observer = mockk<Observer<CurrentWeatherUiState>>(relaxed = true)
        viewModel.uiState.observeForever(observer)

        viewModel.loadSavedCityWeather()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { getCurrentWeatherUseCase(city) }

        verify {
            observer.onChanged(match { it.isLoading })
            observer.onChanged(match { it.weather == mockWeather && !it.isLoading })
        }

        viewModel.uiState.removeObserver(observer)
    }

    @Test
    fun `loadSavedCityWeather does nothing when saved city is blank`() = runTest {
        coEvery { getLastSearchedCityUseCase() } returns Result.Success("")

        viewModel.loadSavedCityWeather()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { getCurrentWeatherUseCase(any()) }
    }

    @Test
    fun `loadCurrentWeather updates uiState with success`() = runTest {
        val city = "Alexandria"
        val weather = CurrentWeather(city, 25.0, "Sunny", "")

        coEvery { getCurrentWeatherUseCase(city) } returns Result.Success(weather)

        val observer = mockk<Observer<CurrentWeatherUiState>>(relaxed = true)
        viewModel.uiState.observeForever(observer)

        viewModel.loadCurrentWeather(city)
        testDispatcher.scheduler.advanceUntilIdle()

        verifySequence {
            observer.onChanged(match { it.isLoading })
            observer.onChanged(match { it.weather == weather && !it.isLoading })
        }

        viewModel.uiState.removeObserver(observer)
    }

    @Test
    fun `loadCurrentWeather updates uiState with error`() = runTest {
        val city = "Giza"
        val error = Result.Error(DataError.Remote.SERVER)

        coEvery { getCurrentWeatherUseCase(city) } returns error

        val observer = mockk<Observer<CurrentWeatherUiState>>(relaxed = true)
        viewModel.uiState.observeForever(observer)

        viewModel.loadCurrentWeather(city)
        testDispatcher.scheduler.advanceUntilIdle()

        verifySequence {
            observer.onChanged(match { it.isLoading })
            observer.onChanged(match { (it.error is UiText) && !it.isLoading })
        }

        viewModel.uiState.removeObserver(observer)
    }
}
