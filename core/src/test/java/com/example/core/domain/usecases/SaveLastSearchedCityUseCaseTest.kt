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
import junit.framework.TestCase.assertTrue

class SaveLastSearchedCityUseCaseTest {

    private val repository = mockk<WeatherRepository>()
    private lateinit var useCase: SaveLastSearchedCityUseCase

    @Before
    fun setup() {
        useCase = SaveLastSearchedCityUseCase(repository)
    }

    @Test
    fun `invoke returns Success when repository saves successfully`() = runTest {
        // Given
        val city = "cairo"
        coEvery { repository.saveLastSearchedCity(city) } returns Result.Success(Unit)

        // When
        val result = useCase(city)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(Unit, (result as Result.Success).data)
    }

    @Test
    fun `invoke returns Error when repository fails to save`() = runTest {
        // Given
        val city = "cairo"
        val error = DataError.Local.UNKNOWN
        coEvery { repository.saveLastSearchedCity(city) } returns Result.Error(error)

        // When
        val result = useCase(city)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).error)
    }
}
