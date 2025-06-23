package com.example.core.domain.usecases

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import com.example.core.domain.repository.WeatherRepository
import org.junit.Before
import org.junit.Test
import com.example.core.domain.Result
import com.example.core.domain.DataError
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue

class GetLastSearchedCityUseCaseTest {

    private val repository = mockk<WeatherRepository>()
    private lateinit var useCase: GetLastSearchedCityUseCase

    @Before
    fun setup() {
        useCase = GetLastSearchedCityUseCase(repository)
    }

    @Test
    fun `invoke returns Success with city name when found`() = runTest {
        // Given
        val expectedCity = "Riyadh"
        coEvery { repository.getLastSearchedCity() } returns Result.Success(expectedCity)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedCity, (result as Result.Success).data)
    }

    @Test
    fun `invoke returns Success with null when no city found`() = runTest {
        // Given
        coEvery { repository.getLastSearchedCity() } returns Result.Success(null)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Success)
        assertNull((result as Result.Success).data)
    }

    @Test
    fun `invoke returns Error when repository fails`() = runTest {
        // Given
        val error = DataError.Local.UNKNOWN
        coEvery { repository.getLastSearchedCity() } returns Result.Error(error)

        // When
        val result = useCase()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).error)
    }
}
