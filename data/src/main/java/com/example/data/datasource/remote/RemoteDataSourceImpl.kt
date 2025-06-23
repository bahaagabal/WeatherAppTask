package com.example.data.datasource.remote

import com.example.core.domain.DataError
import com.example.core.domain.Result
import com.example.data.datasource.RemoteDataSource
import com.example.data.dto.CurrentWeatherDto
import com.example.data.dto.DailyForecastDto
import com.example.data.dto.ForecastDto
import com.example.data.safeCall
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
) : RemoteDataSource {

    companion object {
        private const val API_KEY = "YOUR_API_KEY_HERE"
    }

    override suspend fun getCurrentWeather(city: String): Result<CurrentWeatherDto, DataError.Remote> =
        safeCall {
            weatherApiService.getCurrentWeather(city, API_KEY)
        }


    override suspend fun get7DayForecast(city: String): Result<List<DailyForecastDto>, DataError.Remote> =
        safeCall {
            val response = weatherApiService.get7DayForecast(city, apiKey = API_KEY)
            wrapListResponse(response)
        }

    private fun wrapListResponse(response: Response<*>): Response<List<DailyForecastDto>> {
        val body = (response.body() as? ForecastDto)?.daily
        return Response.success(body)
    }
}