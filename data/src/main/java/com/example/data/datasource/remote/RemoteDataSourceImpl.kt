package com.example.data.datasource.remote

import com.example.core.domain.DataError
import com.example.core.domain.Result
import com.example.data.datasource.RemoteDataSource
import com.example.data.dto.CurrentWeatherDto
import com.example.data.dto.DailyForecastDto
import com.example.data.dto.TempDto
import com.example.data.dto.WeatherDescriptionDto
import com.example.data.safeCall
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
) : RemoteDataSource {

    companion object {
        private const val API_KEY = "9a3049d2163081e844bed41d905b2c84"
    }

    override suspend fun getCurrentWeather(city: String): Result<CurrentWeatherDto, DataError.Remote> =
        safeCall {
            weatherApiService.getCurrentWeather(city, API_KEY)
        }


    /*
        Note this Api is no more free it's required a subscription so I have to mock the response
     *//*
    override suspend fun get7DayForecast(city: String): Result<List<DailyForecastDto>, DataError.Remote> =
        safeCall {
            val response = weatherApiService.get7DayForecast(city, apiKey = API_KEY)
            wrapListResponse(response)
        }

    private fun wrapListResponse(response: Response<*>): Response<List<DailyForecastDto>> {
        val body = (response.body() as? ForecastDto)?.daily
        return Response.success(body)
    }*/

    override suspend fun get7DayForecast(city: String): Result<List<DailyForecastDto>, DataError.Remote> =
        Result.Success(generateFakeForecastList())


    // that is a fake method to mock the response of forecase
    private fun generateFakeForecastList(): List<DailyForecastDto> {
        val weatherOptions = listOf(
            "Thunderstorm" to "11d",
            "Drizzle" to "09d",
            "Rain" to "10d",
            "Snow" to "13d",
            "Mist" to "50d",
            "Clear" to "01d",
            "Few Clouds" to "02d",
            "Scattered Clouds" to "03d",
            "Broken Clouds" to "04d"
        )

        val currentTime = System.currentTimeMillis() / 1000

        return List(7) { index ->
            val (condition, icon) = weatherOptions.random()

            DailyForecastDto(
                date = currentTime + index * 86400, // each day ahead
                temp = TempDto(day = (10..35).random().toDouble()), weather = listOf(
                    WeatherDescriptionDto(
                        condition = condition, icon = icon
                    )
                )
            )
        }
    }

}