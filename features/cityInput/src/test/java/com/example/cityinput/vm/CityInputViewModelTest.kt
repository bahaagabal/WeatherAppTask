package com.example.cityinput.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.core.domain.Result
import com.example.core.domain.usecases.GetLastSearchedCityUseCase
import com.example.core.domain.usecases.SaveLastSearchedCityUseCase
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CityInputViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CityInputViewModel
    private val saveCityUseCase = mockk<SaveLastSearchedCityUseCase>()
    private val getCityUseCase = mockk<GetLastSearchedCityUseCase>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CityInputViewModel(saveCityUseCase, getCityUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onCityChanged updates city LiveData`() {
        viewModel.onCityChanged("Cairo")
        assertEquals("Cairo", viewModel.city.value)
    }

    @Test
    fun `saveCityAndSubmit calls saveCityUseCase and invokes callback`() = runTest {
        val city = "Alexandria"
        var savedCity: String? = null

        coEvery { saveCityUseCase(city) } returns Result.Success(Unit)

        viewModel.onCityChanged(city)
        viewModel.saveCityAndSubmit {
            savedCity = it
        }

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { saveCityUseCase(city) }
        assertEquals(city, savedCity)
    }

    @Test
    fun `loadSavedCity sets city LiveData when getCityUseCase returns success`() = runTest {
        val savedCity = "Aswan"
        coEvery { getCityUseCase() } returns Result.Success(savedCity)

        val observer = mockk<Observer<String>>(relaxed = true)
        viewModel.city.observeForever(observer)

        viewModel.loadSavedCity()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { observer.onChanged(savedCity) }
        viewModel.city.removeObserver(observer)
    }

    @Test
    fun `saveCityAndSubmit does not call useCase when city is blank`() = runTest {
        viewModel.onCityChanged(" ")

        viewModel.saveCityAndSubmit {
            error("Should not be called")
        }

        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { saveCityUseCase(any()) }
    }
}