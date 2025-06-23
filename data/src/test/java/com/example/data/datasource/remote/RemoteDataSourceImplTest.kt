package com.example.data.datasource.remote

import com.example.core.domain.DataError
import com.example.data.dto.CurrentWeatherDto
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import com.example.core.domain.Result
import junit.framework.TestCase.assertNotNull

class RemoteDataSourceImplTest {

    private val apiService = mockk<WeatherApiService>()
    private lateinit var remoteDataSource: RemoteDataSourceImpl

    @Before
    fun setup() {
        remoteDataSource = RemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getCurrentWeather returns Success when API responds correctly`() = runTest {
        // Given
        val city = "Riyadh"
        val dto = CurrentWeatherDto(cityName = "Riyadh", main = null, weather = null)
        val response = Response.success(dto)

        coEvery { apiService.getCurrentWeather(city, any()) } returns response

        // When
        val result = remoteDataSource.getCurrentWeather(city)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(dto, (result as Result.Success).data)
    }

    @Test
    fun `getCurrentWeather returns Error when API fails`() = runTest {
        val city = "Jeddah"
        val response = Response.error<CurrentWeatherDto>(408, okhttp3.ResponseBody.create(null, ""))

        coEvery { apiService.getCurrentWeather(city, any()) } returns response

        val result = remoteDataSource.getCurrentWeather(city)

        assertTrue(result is Result.Error)
        assertEquals(DataError.Remote.REQUEST_TIMEOUT, (result as Result.Error).error)
    }

    // we commented this test cases because we mocked the response
    /*@Test
       fun `get7DayForecast returns Success when API responds correctly`() = runTest {
            val city = "Mecca"
            val forecastDto = ForecastDto(
                city = CityDto(name = "Cairo"),
                daily = listOf(
                    DailyForecastDto(date = 123L, temp = null, weather = null)
                )
            )
            val response = Response.success(forecastDto)

            coEvery { apiService.get7DayForecast(city, apiKey = any()) } returns response

            val result = remoteDataSource.get7DayForecast(city)

            assertTrue(result is Result.Success)
            val data = (result as Result.Success).data
            assertEquals(1, data.size)
            assertEquals(123L, data[0].date)
        }


        @Test
        fun `get7DayForecast returns Error when API fails with timeout`() = runTest {
            val city = "Medina"

            val response = Response.error<ForecastDto>(
                999,
                okhttp3.ResponseBody.create(null, "")
            )

            coEvery {
                apiService.get7DayForecast(
                    city = city,
                    apiKey = any(),
                    count = any(),
                )
            } returns response

            val result = remoteDataSource.get7DayForecast(city)

            assertTrue(result is Result.Error)
            assertEquals(DataError.Remote.UNKNOWN, (result as Result.Error).error)
        } */

    @Test
    fun `get7DayForecast returns 7 days of forecast`() = runTest {
        // When
        val result = remoteDataSource.get7DayForecast("Cairo")

        // Then
        assertTrue(result is Result.Success)
        val forecast = (result as Result.Success).data
        assertEquals(7, forecast.size)

        forecast.forEachIndexed { index, day ->
            assertNotNull(day.date)
            assertNotNull(day.temp)
            assertNotNull(day.weather)

            assertTrue((day.temp?.day ?: 0.0) in 10.0..35.0)
            assertEquals(1, day.weather!!.size)
        }
    }
}
