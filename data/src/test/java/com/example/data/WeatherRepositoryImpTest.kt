package com.example.data

import com.example.core.domain.DataError
import com.example.core.domain.models.CurrentWeather
import com.example.data.datasource.LocalDataSource
import com.example.data.datasource.RemoteDataSource
import com.example.data.dto.CurrentWeatherDto
import com.example.data.mappers.CurrentWeatherMapper
import com.example.data.mappers.DailyForecastMapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.example.core.domain.Result
import com.example.core.domain.models.DailyForecast
import com.example.data.dto.DailyForecastDto

class WeatherRepositoryImpTest {

    private val remoteDataSource: RemoteDataSource = mockk()
    private val localDataSource: LocalDataSource = mockk()
    private val currentWeatherMapper: CurrentWeatherMapper = mockk()
    private val forecastMapper: DailyForecastMapper = mockk()

    private lateinit var repository: WeatherRepositoryImp

    @Before
    fun setup() {
        repository = WeatherRepositoryImp(
            remoteDataSource,
            localDataSource,
            currentWeatherMapper,
            forecastMapper
        )
    }

    @Test
    fun `getCurrentWeather returns mapped weather on success`() = runTest {
        // Given
        val dto = mockk<CurrentWeatherDto>()
        val model = CurrentWeather("Riyadh", 30.0, "Clear", "url")
        coEvery { remoteDataSource.getCurrentWeather("Riyadh") } returns Result.Success(dto)
        every { currentWeatherMapper.map(dto) } returns model

        // When
        val result = repository.getCurrentWeather("Riyadh")

        // Then
        assertTrue(result is Result.Success)
        assertEquals(model, (result as Result.Success).data)
    }

    @Test
    fun `getCurrentWeather returns error when remote fails`() = runTest {
        val error = DataError.Remote.UNKNOWN
        coEvery { remoteDataSource.getCurrentWeather("Riyadh") } returns Result.Error(error)

        val result = repository.getCurrentWeather("Riyadh")

        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).error)
    }

    @Test
    fun `get7DayForecast returns mapped list on success`() = runTest {
        val dtoList = listOf(mockk<DailyForecastDto>(), mockk())
        val mappedList = listOf(
            DailyForecast(123L, 25.0, "Sunny", "iconUrl1"),
            DailyForecast(456L, 22.0, "Cloudy", "iconUrl2")
        )

        coEvery { remoteDataSource.get7DayForecast("Jeddah") } returns Result.Success(dtoList)
        every { forecastMapper.map(dtoList[0]) } returns mappedList[0]
        every { forecastMapper.map(dtoList[1]) } returns mappedList[1]

        val result = repository.get7DayForecast("Jeddah")

        assertTrue(result is Result.Success)
        assertEquals(mappedList, (result as Result.Success).data)
    }

    @Test
    fun `get7DayForecast returns error when remote fails`() = runTest {
        val error = DataError.Remote.SERVER
        coEvery { remoteDataSource.get7DayForecast("Jeddah") } returns Result.Error(error)

        val result = repository.get7DayForecast("Jeddah")

        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).error)
    }

    @Test
    fun `saveLastSearchedCity returns result from local data source`() = runTest {
        val city = "Dammam"
        coEvery { localDataSource.saveLastSearchedCity(city) } returns Result.Success(Unit)

        val result = repository.saveLastSearchedCity(city)

        assertTrue(result is Result.Success)
    }

    @Test
    fun `getLastSearchedCity returns result from local data source`() = runTest {
        val city = "Dammam"
        coEvery { localDataSource.getLastSearchedCity() } returns Result.Success(city)

        val result = repository.getLastSearchedCity()

        assertTrue(result is Result.Success)
        assertEquals(city, (result as Result.Success).data)
    }
}
